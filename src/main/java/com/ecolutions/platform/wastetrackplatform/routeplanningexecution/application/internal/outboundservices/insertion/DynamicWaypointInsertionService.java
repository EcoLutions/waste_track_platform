package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.insertion;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events.ContainerBecameCriticalEvent;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts.MunicipalOperationsContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.PriorityLevel;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.WaypointStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TrafficModel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicWaypointInsertionService {
    private final GeoApiContext geoApiContext;
    private final ContainerRepository containerRepository;
    private final MunicipalOperationsContextFacade municipalOperationsContextFacade;
    private final RouteRepository routeRepository;

    /**
     * Attempt to insert critical container into active route
     */
    public void attemptInsertCriticalContainer(Route route, ContainerBecameCriticalEvent event) {
        Container criticalContainer = containerRepository.findById(event.getContainerId())
                .orElseThrow(() -> new IllegalArgumentException("Container not found: " + event.getContainerId()));

        DistrictConfigDTO districtConfig = municipalOperationsContextFacade
                .getDistrictConfiguration(route.getDistrictId().value())
                .orElseThrow(() -> new IllegalArgumentException("District not found: " + route.getDistrictId()));

        // Get remaining waypoints (not yet visited)
        List<WayPoint> remainingWaypoints = route.getWaypoints().stream()
                .filter(wp -> wp.getStatus() == WaypointStatus.PENDING)
                .sorted(Comparator.comparing(WayPoint::getSequenceOrder))
                .toList();

        if (remainingWaypoints.isEmpty()) {
            log.warn("No remaining waypoints in route {}, cannot insert", route.getId());
            notifyAdmin(route, "Container " + event.getContainerId() + " is CRITICAL but route has no pending waypoints");
            return;
        }

        // Scenario 1: Try to insert without replacing
        InsertionResult insertionResult = tryInsertWithoutReplacing(route, remainingWaypoints, criticalContainer, districtConfig.maxRouteDuration());

        if (insertionResult.success()) {
            applyInsertion(route, insertionResult);
            notifyDriver(route, "New CRITICAL container added to your route at position " + insertionResult.insertionPosition());
            log.info("Successfully inserted container {} into route {} at position {}", event.getContainerId(), route.getId(), insertionResult.insertionPosition());
            return;
        }

        // Scenario 2: Try to replace the lowest priority waypoint
        InsertionResult replacementResult = tryReplaceLowestPriority(route, remainingWaypoints, criticalContainer, districtConfig.maxRouteDuration());

        if (replacementResult.success()) {
            applyReplacement(route, replacementResult);
            notifyDriver(route, "Waypoint replaced by CRITICAL container");
            log.info("Successfully replaced waypoint {} with container {} in route {}", replacementResult.replacedWaypointId(), event.getContainerId(), route.getId());
            return;
        }

        // Cannot insert
        log.warn("Cannot insert container {} into route {}: exceeds time constraints", event.getContainerId(), route.getId());
        notifyAdmin(route, "Container CRITICAL " + event.getContainerId() + " could not be added to route (time constraint)");
    }

    /**
     * Try to insert without replacing any existing waypoint
     */
    private InsertionResult tryInsertWithoutReplacing(Route route, List<WayPoint> remainingWaypoints, Container criticalContainer, Duration maxDuration) {
        Location currentLocation = route.getCurrentLocation();
        if (currentLocation == null) {
            // Route hasn't started tracking, use the first waypoint location
            currentLocation = containerRepository
                    .findById(remainingWaypoints.getFirst().getContainerId().value())
                    .map(Container::getLocation)
                    .orElse(null);
        }

        // Try inserting at each position
        for (int insertPos = 0; insertPos < remainingWaypoints.size(); insertPos++) {
            List<WayPoint> modifiedWaypoints = new ArrayList<>(remainingWaypoints);
            WayPoint newWaypoint = createWaypoint(criticalContainer, insertPos + 1);
            modifiedWaypoints.add(insertPos, newWaypoint);

            // Recalculate duration
            Duration newDuration = calculateRouteDuration(currentLocation, modifiedWaypoints);

            if (newDuration.compareTo(maxDuration) <= 0) {
                return InsertionResult.success(insertPos, newWaypoint, newDuration);
            }
        }

        return InsertionResult.failure();
    }

    /**
     * Try to replace the lowest priority waypoint
     */
    private InsertionResult tryReplaceLowestPriority(Route route, List<WayPoint> remainingWaypoints, Container criticalContainer, Duration maxDuration) {
        // Find the lowest priority waypoint (not CRITICAL)
        WayPoint lowestPriority = remainingWaypoints.stream()
                .filter(wp -> wp.getPriority().level() != PriorityLevel.CRITICAL)
                .min(Comparator.comparing(wp -> wp.getPriority().level()))
                .orElse(null);

        if (lowestPriority == null) {
            log.info("All remaining waypoints are CRITICAL, cannot replace");
            return InsertionResult.failure();
        }

        // Replace and recalculate
        List<WayPoint> modifiedWaypoints = new ArrayList<>(remainingWaypoints);
        modifiedWaypoints.remove(lowestPriority);

        int position = lowestPriority.getSequenceOrder() - 1;
        WayPoint newWaypoint = createWaypoint(criticalContainer, lowestPriority.getSequenceOrder());

        if (position >= modifiedWaypoints.size()) {
            modifiedWaypoints.add(newWaypoint);
        } else {
            modifiedWaypoints.add(position, newWaypoint);
        }

        Location currentLocation = route.getCurrentLocation();
        Duration newDuration = calculateRouteDuration(currentLocation, modifiedWaypoints);

        if (newDuration.compareTo(maxDuration) <= 0) {
            return InsertionResult.replacement(lowestPriority.getId(), newWaypoint, newDuration);
        }

        return InsertionResult.failure();
    }

    /**
     * Calculate route duration using Google Maps
     */
    private Duration calculateRouteDuration(Location start, List<WayPoint> waypoints) {
        if (start == null || waypoints.isEmpty()) {
            return Duration.ofMinutes(waypoints.size() * 8L); // Fallback estimation
        }

        try {
            List<LatLng> locations = waypoints.stream()
                    .map(wp -> containerRepository.findById(wp.getContainerId().value())
                            .map(c -> c.getLocation().toGoogleLatLng())
                            .orElseThrow())
                    .toList();

            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(start.toGoogleLatLng())
                    .waypoints(locations.toArray(new LatLng[0]))
                    .destination(locations.getLast())
                    .departureTime(Instant.now())
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .await();

            return Duration.ofSeconds(
                    Arrays.stream(result.routes[0].legs)
                            .mapToLong(leg -> leg.duration.inSeconds)
                            .sum()
            );
        } catch (Exception e) {
            log.warn("Error calculating route duration, using fallback estimation", e);
            return Duration.ofMinutes(waypoints.size() * 8L);
        }
    }

    /**
     * Create waypoint for container
     */
    private WayPoint createWaypoint(Container container, int sequenceOrder) {
        CreateWayPointCommand command = new CreateWayPointCommand(
                null, // routeId will be set later
                container.getId(),
                sequenceOrder,
                PriorityLevel.CRITICAL.name()
        );
        return new WayPoint(command);
    }

    /**
     * Apply insertion to route
     */
    private void applyInsertion(Route route, InsertionResult result) {
        WayPoint newWaypoint = result.newWaypoint();
        route.addWayPoint(newWaypoint);
        route.updateEstimates(null, result.newDuration());
        routeRepository.save(route);
        log.info("Inserted new waypoint into route {}", route.getId());
    }

    /**
     * Apply replacement to route
     */
    private void applyReplacement(Route route, InsertionResult result) {
        route.removeWaypoint(result.replacedWaypointId());
        route.addWayPoint(result.newWaypoint());
        route.updateEstimates(null, result.newDuration());
        routeRepository.save(route);
        log.info("Replaced waypoint {} in route {}", result.replacedWaypointId(), route.getId());
    }

    /**
     * Notify driver (stub - implement with WebSocket/SMS)
     */
    private void notifyDriver(Route route, String message) {
        log.info("[DRIVER NOTIFICATION] Route {}: {}", route.getId(), message);
        // TODO: Implement WebSocket push notification or SMS
    }

    /**
     * Notify admin (stub - implement with email/dashboard)
     */
    private void notifyAdmin(Route route, String message) {
        log.warn("[ADMIN NOTIFICATION] Route {}: {}", route.getId(), message);
        // TODO: Implement admin notification
    }

    /**
     * Result of an insertion attempt
     */
    @Builder
    private record InsertionResult(boolean success, Integer insertionPosition, String replacedWaypointId, WayPoint newWaypoint, Duration newDuration) {
        static InsertionResult success(int position, WayPoint waypoint, Duration duration) {
            return InsertionResult.builder()
                    .success(true)
                    .insertionPosition(position)
                    .newWaypoint(waypoint)
                    .newDuration(duration)
                    .build();
        }

        static InsertionResult replacement(String replacedId, WayPoint waypoint, Duration duration) {
            return InsertionResult.builder()
                    .success(true)
                    .replacedWaypointId(replacedId)
                    .newWaypoint(waypoint)
                    .newDuration(duration)
                    .build();
        }

        static InsertionResult failure() {
            return InsertionResult.builder().success(false).build();
        }
    }
}
