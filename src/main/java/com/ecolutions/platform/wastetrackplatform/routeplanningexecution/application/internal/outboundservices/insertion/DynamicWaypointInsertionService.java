package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.insertion;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events.ContainerBecameCriticalEvent;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.contexts.ContainerMonitoringContextFacade;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.dtos.ContainerInfoDTO;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts.MunicipalOperationsContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.RouteWebSocketPublisherService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.transform.WaypointPayloadAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events.CriticalContainerNotAddedEvent;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events.RouteWaypointAddedEvent;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events.RouteWaypointReplacedEvent;
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
import org.springframework.context.ApplicationEventPublisher;
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
    private final ContainerMonitoringContextFacade containerMonitoringFacade;
    private final MunicipalOperationsContextFacade municipalOperationsContextFacade;
    private final RouteRepository routeRepository;
    private final RouteWebSocketPublisherService routeWebSocketPublisher;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Attempt to insert critical container into active route
     */
    public void attemptInsertCriticalContainer(Route route, ContainerBecameCriticalEvent event) {
        ContainerInfoDTO criticalContainer = containerMonitoringFacade.getContainerInfo(event.getContainerId())
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
            publishCriticalContainerNotAdded(route, criticalContainer, "Container is CRITICAL but route has no pending waypoints");
            return;
        }

        // Scenario 1: Try to insert without replacing
        InsertionResult insertionResult = tryInsertWithoutReplacing(route, remainingWaypoints, criticalContainer, districtConfig.maxRouteDuration());

        if (insertionResult.success()) {
            applyInsertion(route, criticalContainer, insertionResult);
            log.info("Successfully inserted container {} into route {} at position {}", event.getContainerId(), route.getId(), insertionResult.insertionPosition());
            return;
        }

        // Scenario 2: Try to replace the lowest priority waypoint
        InsertionResult replacementResult = tryReplaceLowestPriority(route, remainingWaypoints, criticalContainer, districtConfig.maxRouteDuration());

        if (replacementResult.success()) {
            applyReplacement(route, criticalContainer, replacementResult);
            log.info("Successfully replaced waypoint {} with container {} in route {}", replacementResult.replacedWaypointId(), event.getContainerId(), route.getId());
            return;
        }

        // Cannot insert
        log.warn("Cannot insert container {} into route {}: exceeds time constraints", event.getContainerId(), route.getId());
        publishCriticalContainerNotAdded(route, criticalContainer, "Container could not be added to route (time constraint)");
    }

    /**
     * Try to insert without replacing any existing waypoint
     */
    private InsertionResult tryInsertWithoutReplacing(Route route, List<WayPoint> remainingWaypoints, ContainerInfoDTO criticalContainer, Duration maxDuration) {
        Location currentLocation = route.getCurrentLocation();
        if (currentLocation == null) {
            // Route hasn't started tracking, use the first waypoint location
            currentLocation = containerMonitoringFacade
                    .getContainerInfo(remainingWaypoints.getFirst().getContainerId().value())
                    .map(ContainerInfoDTO::location)
                    .orElse(null);
        }

        // Try inserting at each position
        for (int insertPos = 0; insertPos < remainingWaypoints.size(); insertPos++) {
            List<WayPoint> modifiedWaypoints = new ArrayList<>(remainingWaypoints);
            WayPoint newWaypoint = createWaypoint(route.getId(), criticalContainer, insertPos + 1);
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
    private InsertionResult tryReplaceLowestPriority(Route route, List<WayPoint> remainingWaypoints, ContainerInfoDTO criticalContainer, Duration maxDuration) {
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
        WayPoint newWaypoint = createWaypoint(route.getId(), criticalContainer, lowestPriority.getSequenceOrder());

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
                    .map(wp -> containerMonitoringFacade.getContainerInfo(wp.getContainerId().value())
                            .map(c -> c.location().toGoogleLatLng())
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
    private WayPoint createWaypoint(String routeId, ContainerInfoDTO container, int sequenceOrder) {
        CreateWayPointCommand command = new CreateWayPointCommand(
                routeId,
                container.containerId(),
                sequenceOrder,
                PriorityLevel.CRITICAL.name()
        );
        return new WayPoint(command);
    }

    /**
     * Apply insertion to route
     */
    private void applyInsertion(Route route, ContainerInfoDTO container, InsertionResult result) {
        WayPoint newWaypoint = result.newWaypoint();
        route.addWayPoint(newWaypoint);
        route.updateEstimates(null, result.newDuration());
        routeRepository.save(route);

        // Publish WebSocket notification for route-specific updates
        var waypointAddedPayload = WaypointPayloadAssembler.toAddedPayload(
                route.getId(),
                newWaypoint,
                container,
                "Container became CRITICAL and was added to route"
        );
        routeWebSocketPublisher.publishWaypointAdded(waypointAddedPayload);

        // Publish domain event for a notification system
        RouteWaypointAddedEvent event = RouteWaypointAddedEvent.builder()
                .source(this)
                .routeId(route.getId())
                .driverId(route.getDriverId() != null ? route.getDriverId().value() : null)
                .districtId(route.getDistrictId().value())
                .waypointId(newWaypoint.getId())
                .containerId(container.containerId())
                .containerLocation(container.location())
                .fillLevel(container.fillLevelPercentage())
                .priority(newWaypoint.getPriority().level().name())
                .sequenceOrder(newWaypoint.getSequenceOrder())
                .reason("Container became CRITICAL and was added to route")
                .build();
        eventPublisher.publishEvent(event);

        log.info("Inserted new waypoint into route {}", route.getId());
    }

    /**
     * Apply replacement to route
     */
    private void applyReplacement(Route route, ContainerInfoDTO addedContainer, InsertionResult result) {
        // Get removed waypoint and container info before removal
        WayPoint removedWaypoint = route.getWaypoints().stream()
                .filter(wp -> wp.getId().equals(result.replacedWaypointId()))
                .findFirst()
                .orElseThrow();
        ContainerInfoDTO removedContainer = containerMonitoringFacade.getContainerInfo(removedWaypoint.getContainerId().value()).orElseThrow();

        route.removeWaypoint(result.replacedWaypointId());
        route.addWayPoint(result.newWaypoint());
        route.updateEstimates(null, result.newDuration());
        routeRepository.save(route);

        // Publish WebSocket notification for route-specific updates
        var waypointReplacedPayload = WaypointPayloadAssembler.toReplacedPayload(
                route.getId(),
                removedWaypoint,
                removedContainer,
                result.newWaypoint(),
                addedContainer,
                "Lower priority waypoint replaced by CRITICAL container"
        );
        routeWebSocketPublisher.publishWaypointReplaced(waypointReplacedPayload);

        // Publish domain event for a notification system
        RouteWaypointReplacedEvent event = RouteWaypointReplacedEvent.builder()
                .source(this)
                .routeId(route.getId())
                .driverId(route.getDriverId() != null ? route.getDriverId().value() : null)
                .districtId(route.getDistrictId().value())
                .removedWaypointId(removedWaypoint.getId())
                .removedContainerId(removedContainer.containerId())
                .removedContainerLocation(removedContainer.location())
                .removedPriority(removedWaypoint.getPriority().level().name())
                .addedWaypointId(result.newWaypoint().getId())
                .addedContainerId(addedContainer.containerId())
                .addedContainerLocation(addedContainer.location())
                .addedFillLevel(addedContainer.fillLevelPercentage())
                .addedPriority(result.newWaypoint().getPriority().level().name())
                .reason("Lower priority waypoint replaced by CRITICAL container")
                .build();
        eventPublisher.publishEvent(event);

        log.info("Replaced waypoint {} in route {}", result.replacedWaypointId(), route.getId());
    }

    /**
     * Publish event when a critical container cannot be added to a route
     */
    private void publishCriticalContainerNotAdded(Route route, ContainerInfoDTO container, String reason) {
        log.warn("[CRITICAL CONTAINER NOT ADDED] Route {}: {}", route.getId(), reason);

        CriticalContainerNotAddedEvent event = CriticalContainerNotAddedEvent.builder()
                .source(this)
                .routeId(route.getId())
                .districtId(route.getDistrictId().value())
                .containerId(container.containerId())
                .containerLocation(container.location())
                .fillLevel(container.fillLevelPercentage())
                .reason(reason)
                .build();

        eventPublisher.publishEvent(event);
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
