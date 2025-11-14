package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.optimization;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.contexts.ContainerMonitoringContextFacade;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.dtos.ContainerInfoDTO;
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
    private final ContainerMonitoringContextFacade containerMonitoringFacade;
    private final MunicipalOperationsContextFacade municipalOperationsContextFacade;

    // Limit for Google Maps Api
    private static final int MAX_WAYPOINTS = 25;

    /**
     * Optimize route for a given district and scheduled time
     */
    public OptimizedRouteResult optimizeRoute(String districtId, LocalDateTime scheduledStartAt) {
        DistrictConfigDTO districtConfig = fetchDistrictConfiguration(districtId);
        List<ContainerInfoDTO> candidates = getAndSortContainerCandidates(districtId);
        RouteLocations locations = extractDepotAndDisposalLocations(districtConfig);

        try {
            DirectionsResult result = callGoogleMapsDirections(locations.depot(), locations.disposal(), candidates, scheduledStartAt);
            RouteDurations durations = calculateRouteDurations(result, locations, scheduledStartAt);

            OptimizedRouteData optimizedData = adjustWaypointsToFitTimeConstraints(candidates, locations, districtConfig.maxRouteDuration(), scheduledStartAt, result, durations);

            return buildOptimizedResult(candidates, optimizedData.result(), optimizedData.durations(), scheduledStartAt);

        } catch (Exception e) {
            log.error("Error calling Google Maps API, using fallback algorithm", e);
            return optimizeWithFallbackAlgorithm(candidates, scheduledStartAt);
        }
    }

    private DistrictConfigDTO fetchDistrictConfiguration(String districtId) {
        return municipalOperationsContextFacade
                .getDistrictConfiguration(districtId)
                .orElseThrow(() -> new IllegalArgumentException("District not found: " + districtId));
    }

    private List<ContainerInfoDTO> getAndSortContainerCandidates(String districtId) {
        List<ContainerInfoDTO> candidates = containerMonitoringFacade.getContainersRequiringCollection(DistrictId.of(districtId));

        List<ContainerInfoDTO> sortedCandidates = new ArrayList<>(candidates);
        sortedCandidates.sort(Comparator.comparing(ContainerInfoDTO::fillLevelPercentage).reversed());

        if (sortedCandidates.size() > MAX_WAYPOINTS) {
            sortedCandidates = sortedCandidates.subList(0, MAX_WAYPOINTS);
        }

        if (sortedCandidates.isEmpty()) {
            throw new IllegalArgumentException("No containers require collection in district: " + districtId);
        }

        return sortedCandidates;
    }

    private RouteLocations extractDepotAndDisposalLocations(DistrictConfigDTO config) {
        Location depotLocation = Location.fromBigDecimal(
                config.depotLatitude(),
                config.depotLongitude()
        );

        Location disposalLocation = config.disposalLatitude() != null
                ? Location.fromBigDecimal(config.disposalLatitude(), config.disposalLongitude())
                : depotLocation;

        return new RouteLocations(depotLocation, disposalLocation);
    }

    private RouteDurations calculateRouteDurations(DirectionsResult result, RouteLocations locations, LocalDateTime scheduledStartAt) {
        Duration collectionDuration = calculateTotalDuration(result);
        Duration returnDuration = calculateReturnDuration(locations.disposal(), locations.depot(), scheduledStartAt, collectionDuration);
        Duration totalDuration = collectionDuration.plus(returnDuration).plusMinutes(15); // Add buffer

        return new RouteDurations(collectionDuration, returnDuration, totalDuration);
    }

    private OptimizedRouteData adjustWaypointsToFitTimeConstraints(List<ContainerInfoDTO> candidates, RouteLocations locations, Duration maxRouteDuration, LocalDateTime scheduledStartAt, DirectionsResult result, RouteDurations durations) throws Exception {

        while (durations.total().compareTo(maxRouteDuration) > 0 && candidates.size() > 1) {
            log.info("Route exceeds maxRouteDuration ({}), removing lowest priority container", maxRouteDuration);

            removeLowestPriorityContainer(candidates);

            result = callGoogleMapsDirections(locations.depot(), locations.disposal(), candidates, scheduledStartAt);
            durations = calculateRouteDurations(result, locations, scheduledStartAt);
        }

        return new OptimizedRouteData(result, durations);
    }

    private void removeLowestPriorityContainer(List<ContainerInfoDTO> candidates) {
        ContainerInfoDTO lowestPriority = candidates.stream()
                .min(Comparator.comparing(ContainerInfoDTO::fillLevelPercentage))
                .orElseThrow();
        candidates.remove(lowestPriority);
    }

    private OptimizedRouteResult buildOptimizedResult(List<ContainerInfoDTO> candidates, DirectionsResult result, RouteDurations durations, LocalDateTime scheduledStartAt) {

        List<WayPoint> waypoints = createOptimizedWaypoints(candidates, result);
        Distance totalDistance = calculateTotalDistance(result);

        return OptimizedRouteResult.builder()
                .waypoints(waypoints)
                .totalDuration(durations.total())
                .collectionDuration(durations.collection())
                .returnDuration(durations.returnDuration())
                .totalDistance(totalDistance)
                .scheduledEndAt(scheduledStartAt.plus(durations.total()))
                .encodedPolyline(result.routes[0].overviewPolyline.getEncodedPath())
                .build();
    }

    private record RouteLocations(Location depot, Location disposal) {}
    private record RouteDurations(Duration collection, Duration returnDuration, Duration total) {}
    private record OptimizedRouteData(DirectionsResult result, RouteDurations durations) {}

    /**
     * Map fill level to PriorityLevel enum
     */
    public PriorityLevel calculatePriorityLevel(Integer fillLevelPercentage) {
        if (fillLevelPercentage >= 90) return PriorityLevel.CRITICAL;
        if (fillLevelPercentage >= 80) return PriorityLevel.HIGH;
        if (fillLevelPercentage >= 70) return PriorityLevel.MEDIUM;
        return PriorityLevel.LOW;
    }

    /**
     * Call Google Maps Directions API
     */
    private DirectionsResult callGoogleMapsDirections(Location origin, Location destination, List<ContainerInfoDTO> containers, LocalDateTime scheduledStartAt) throws Exception {
        // Build waypoints
        DirectionsApiRequest.Waypoint[] waypoints = containers.stream()
                .map(c -> new DirectionsApiRequest.Waypoint(c.location().toGoogleLatLng()))
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
    private List<WayPoint> createOptimizedWaypoints(List<ContainerInfoDTO> containers, DirectionsResult result) {
        List<WayPoint> waypoints = new ArrayList<>();
        int[] waypointOrder = result.routes[0].waypointOrder;

        for (int i = 0; i < waypointOrder.length; i++) {
            ContainerInfoDTO container = containers.get(waypointOrder[i]);
            PriorityLevel priority = calculatePriorityLevel(container.fillLevelPercentage());

            CreateWayPointCommand command = new CreateWayPointCommand(
                    null, // routeId will be set later
                    container.containerId(),
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
    private OptimizedRouteResult optimizeWithFallbackAlgorithm(List<ContainerInfoDTO> containers, LocalDateTime scheduledStartAt) {
        log.info("Using fallback greedy algorithm for route optimization");

        // Simple estimation: 8 minutes per container + 5 minutes per km
        Duration estimatedDuration = Duration.ofMinutes(containers.size() * 8L);
        Distance estimatedDistance = Distance.fromMeters(containers.size() * 500L); // 500m between containers

        List<WayPoint> waypoints = new ArrayList<>();
        for (int i = 0; i < containers.size(); i++) {
            ContainerInfoDTO container = containers.get(i);
            CreateWayPointCommand command = new CreateWayPointCommand(
                    null,
                    container.containerId(),
                    i + 1,
                    calculatePriorityLevel(container.fillLevelPercentage()).name()
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
