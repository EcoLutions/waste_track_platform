package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.optimization;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.ContainerStatus;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts.MunicipalOperationsContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.Distance;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.PriorityLevel;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TrafficModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteOptimizationService {
    private final GeoApiContext geoApiContext;
    private final ContainerRepository containerRepository;
    private final MunicipalOperationsContextFacade municipalOperationsContextFacade;

    // Limit for Google Maps Api
    private static final int MAX_WAYPOINTS = 25;

    /**
     * Optimize route for a given district and scheduled time
     */
    public OptimizedRouteResult optimizeRoute(String districtId, LocalDateTime scheduledStartAt) {
        // 1. Get district configuration
        DistrictConfigDTO districtConfig = municipalOperationsContextFacade
                .getDistrictConfiguration(districtId)
                .orElseThrow(() -> new IllegalArgumentException("District not found: " + districtId));

        // 2. Get container candidates (require collection)
        List<Container> candidates = getContainerCandidates(DistrictId.of(districtId));

        // 3. Sort by calculated priority (create mutable copy)
        List<Container> sortedCandidates = new ArrayList<>(candidates);
        sortedCandidates.sort(Comparator.comparing(this::calculateContainerPriority).reversed());

        // 4. Limit to MAX_WAYPOINTS
        if (candidates.size() > MAX_WAYPOINTS) {
            candidates = candidates.subList(0, MAX_WAYPOINTS);
        }

        if (candidates.isEmpty())
            throw new IllegalArgumentException("No containers require collection in district: " + districtId);

        // 5. Build locations for Google Maps
        Location depotLocation = Location.fromBigDecimal(
                districtConfig.depotLatitude(),
                districtConfig.depotLongitude()
        );

        Location disposalLocation = districtConfig.disposalLatitude() != null
                ? Location.fromBigDecimal(districtConfig.disposalLatitude(), districtConfig.disposalLongitude())
                : depotLocation;

        // 6. Call Google Maps API with a pessimistic traffic model
        try {
            DirectionsResult result = callGoogleMapsDirections(depotLocation, disposalLocation, candidates, scheduledStartAt);

            // 7. Calculate durations
            Duration collectionDuration = calculateTotalDuration(result);
            Duration returnDuration = calculateReturnDuration(disposalLocation, depotLocation, scheduledStartAt, collectionDuration);
            Duration totalDuration = collectionDuration.plus(returnDuration).plusMinutes(15); // Add buffer

            // 8. Adjust waypoints if exceeds maxRouteDuration
            while (totalDuration.compareTo(districtConfig.maxRouteDuration()) > 0 && candidates.size() > 1) {
                log.info("Route exceeds maxRouteDuration ({}), removing lowest priority container", districtConfig.maxRouteDuration());

                // Remove the lowest priority container
                Container lowestPriority = candidates.stream()
                        .min(Comparator.comparing(this::calculateContainerPriority))
                        .orElseThrow();
                candidates.remove(lowestPriority);

                // Recalculate
                result = callGoogleMapsDirections(depotLocation, disposalLocation, candidates, scheduledStartAt);
                collectionDuration = calculateTotalDuration(result);
                returnDuration = calculateReturnDuration(disposalLocation, depotLocation, scheduledStartAt, collectionDuration);
                totalDuration = collectionDuration.plus(returnDuration).plusMinutes(15);
            }

            // 9. Create waypoints with an optimized sequence
            List<WayPoint> waypoints = createOptimizedWaypoints(candidates, result);

            // 10. Calculate total distance
            Distance totalDistance = calculateTotalDistance(result);

            // 11. Build result
            return OptimizedRouteResult.builder()
                    .waypoints(waypoints)
                    .totalDuration(totalDuration)
                    .collectionDuration(collectionDuration)
                    .returnDuration(returnDuration)
                    .totalDistance(totalDistance)
                    .scheduledEndAt(scheduledStartAt.plus(totalDuration))
                    .encodedPolyline(result.routes[0].overviewPolyline.getEncodedPath())
                    .build();

        } catch (Exception e) {
            log.error("Error calling Google Maps API, using fallback algorithm", e);
            return optimizeWithFallbackAlgorithm(districtId, candidates, scheduledStartAt);
        }
    }

    /**
     * Get containers that require collection
     */
    private List<Container> getContainerCandidates(DistrictId districtId) {
        List<Container> allContainers = containerRepository.findAllByDistrictId(districtId);
        return allContainers.stream()
                .filter(c -> c.getStatus() == ContainerStatus.ACTIVE)
                .filter(Container::requiresCollection)
                .toList();
    }

    /**
     * Calculate priority score for container
     * Higher score = higher priority
     */
    private int calculateContainerPriority(Container container) {
        int score = container.getCurrentFillLevel().percentage();

        // Bonus for overflowing
        if (container.isOverflowing()) {
            score += 50;
        }

        // Bonus for days since the last collection
        if (container.getLastCollectionDate() != null) {
            long daysSinceCollection = java.time.temporal.ChronoUnit.DAYS.between(
                    container.getLastCollectionDate(),
                    LocalDateTime.now()
            );
            score += (int) Math.min(daysSinceCollection * 2, 30);
        }

        return score;
    }

    /**
     * Map container priority to PriorityLevel enum
     */
    public PriorityLevel calculatePriorityLevel(Container container) {
        int fillLevel = container.getCurrentFillLevel().percentage();

        if (fillLevel >= 90) return PriorityLevel.CRITICAL;
        if (fillLevel >= 80) return PriorityLevel.HIGH;
        if (fillLevel >= 70) return PriorityLevel.MEDIUM;
        return PriorityLevel.LOW;
    }

    /**
     * Call Google Maps Directions API
     */
    private DirectionsResult callGoogleMapsDirections(Location origin, Location destination, List<Container> containers, LocalDateTime scheduledStartAt) throws Exception {
        // Build waypoints
        DirectionsApiRequest.Waypoint[] waypoints = containers.stream()
                .map(c -> new DirectionsApiRequest.Waypoint(c.getLocation().toGoogleLatLng()))
                .toArray(DirectionsApiRequest.Waypoint[]::new);

        // Convert to Instant
        Instant departureTime = scheduledStartAt.atZone(ZoneId.systemDefault()).toInstant();

        return DirectionsApi.newRequest(geoApiContext)
                .origin(origin.toGoogleLatLng())
                .waypoints(waypoints)
                .destination(destination.toGoogleLatLng())
                .optimizeWaypoints(true)
                .departureTime(departureTime)
                .trafficModel(TrafficModel.PESSIMISTIC) // Conservative estimate
                .await();
    }

    /**
     * Calculate total duration from DirectionsResult
     */
    private Duration calculateTotalDuration(DirectionsResult result) {
        long totalSeconds = Arrays.stream(result.routes[0].legs)
                .mapToLong(leg -> leg.duration.inSeconds)
                .sum();
        return Duration.ofSeconds(totalSeconds);
    }

    /**
     * Calculate return trip duration (disposal -> depot)
     */
    private Duration calculateReturnDuration(Location disposalLocation, Location depotLocation, LocalDateTime scheduledStartAt, Duration collectionDuration) {
        // If disposal == depot, no return trip needed
        if (disposalLocation.equals(depotLocation)) {
            return Duration.ZERO;
        }

        try {
            Instant arrivalAtDisposal = scheduledStartAt
                    .plus(collectionDuration)
                    .atZone(ZoneId.systemDefault())
                    .toInstant();

            DirectionsResult returnResult = DirectionsApi.newRequest(geoApiContext)
                    .origin(disposalLocation.toGoogleLatLng())
                    .destination(depotLocation.toGoogleLatLng())
                    .departureTime(arrivalAtDisposal)
                    .trafficModel(TrafficModel.PESSIMISTIC)
                    .await();

            return Duration.ofSeconds(returnResult.routes[0].legs[0].duration.inSeconds);
        } catch (Exception e) {
            log.warn("Error calculating return duration, using estimate", e);
            // Fallback: estimate based on distance (~30 km/h average)
            double distanceKm = depotLocation.distanceTo(disposalLocation);
            return Duration.ofMinutes((long) (distanceKm / 0.5));
        }
    }

    /**
     * Calculate total distance from DirectionsResult
     */
    private Distance calculateTotalDistance(DirectionsResult result) {
        long totalMeters = Arrays.stream(result.routes[0].legs)
                .mapToLong(leg -> leg.distance.inMeters)
                .sum();
        return Distance.fromMeters(totalMeters);
    }

    /**
     * Create optimized waypoints based on a Google Maps result
     */
    private List<WayPoint> createOptimizedWaypoints(List<Container> containers, DirectionsResult result) {
        List<WayPoint> waypoints = new ArrayList<>();
        int[] waypointOrder = result.routes[0].waypointOrder;

        for (int i = 0; i < waypointOrder.length; i++) {
            Container container = containers.get(waypointOrder[i]);
            PriorityLevel priority = calculatePriorityLevel(container);

            CreateWayPointCommand command = new CreateWayPointCommand(
                    null, // routeId will be set later
                    container.getId(),
                    i + 1, // sequence order
                    priority.name()
            );

            waypoints.add(new WayPoint(command));
        }

        return waypoints;
    }

    /**
     * Fallback algorithm if Google Maps fails (simple greedy)
     */
    private OptimizedRouteResult optimizeWithFallbackAlgorithm(String districtId, List<Container> containers, LocalDateTime scheduledStartAt) {
        log.info("Using fallback greedy algorithm for route optimization");

        DistrictConfigDTO districtConfig = municipalOperationsContextFacade
                .getDistrictConfiguration(districtId)
                .orElseThrow();

        // Simple estimation: 8 minutes per container + 5 minutes per km
        Duration estimatedDuration = Duration.ofMinutes(containers.size() * 8L);
        Distance estimatedDistance = Distance.fromMeters(containers.size() * 500L); // 500m between containers

        List<WayPoint> waypoints = new ArrayList<>();
        for (int i = 0; i < containers.size(); i++) {
            Container container = containers.get(i);
            CreateWayPointCommand command = new CreateWayPointCommand(
                    null,
                    container.getId(),
                    i + 1,
                    calculatePriorityLevel(container).name()
            );
            waypoints.add(new WayPoint(command));
        }

        return OptimizedRouteResult.builder()
                .waypoints(waypoints)
                .totalDuration(estimatedDuration)
                .collectionDuration(estimatedDuration)
                .returnDuration(Duration.ZERO)
                .totalDistance(estimatedDistance)
                .scheduledEndAt(scheduledStartAt.plus(estimatedDuration))
                .encodedPolyline(null)
                .build();
    }
}
