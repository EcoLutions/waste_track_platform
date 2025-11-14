package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.update;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.contexts.ContainerMonitoringContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts.MunicipalOperationsContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.RouteWebSocketPublisherService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.transform.WaypointPayloadAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events.RouteWaypointRemovedEvent;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.Distance;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.PriorityLevel;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.WaypointStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TrafficModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteUpdateService {
    private final GeoApiContext geoApiContext;
    private final ContainerMonitoringContextFacade containerMonitoringFacade;
    private final MunicipalOperationsContextFacade municipalOperationsContextFacade;
    private final RouteRepository routeRepository;
    private final RouteWebSocketPublisherService routeWebSocketPublisher;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Determine if the route needs update based on time since the last update
     */
    public boolean shouldUpdate(Route route) {
        if (route.getLastLocationUpdate() == null) {
            return true; // Never updated
        }

        Duration timeSinceLastUpdate = Duration.between(route.getLastLocationUpdate(), LocalDateTime.now());

        // Adaptive intervals based on progress
        if (route.getProgressPercentage() < 20) {
            return timeSinceLastUpdate.toMinutes() >= 10; // Start: every 10 min
        } else if (route.getProgressPercentage() < 80) {
            return timeSinceLastUpdate.toMinutes() >= 10; // Middle: every 10 min
        } else {
            return timeSinceLastUpdate.toMinutes() >= 10; // End: every 10 min
        }
    }

    /**
     * Update route estimates with current traffic
     */
    public void updateRouteEstimates(Route route) {
        log.info("Updating estimates for route {}", route.getId());

        // Get remaining waypoints
        List<WayPoint> remainingWaypoints = route.getWaypoints().stream()
                .filter(wp -> wp.getStatus() == WaypointStatus.PENDING)
                .sorted(Comparator.comparing(WayPoint::getSequenceOrder))
                .toList();

        if (remainingWaypoints.isEmpty()) {
            log.info("No remaining waypoints in route {}, skipping update", route.getId());
            return;
        }

        Location currentLocation = route.getCurrentLocation();
        if (currentLocation == null) {
            log.warn("Route {} has no current location, cannot update", route.getId());
            return;
        }

        try {
            // Call Google Maps with current traffic
            DirectionsResult result = recalculateWithCurrentTraffic(currentLocation, remainingWaypoints);

            // Calculate new durations
            Duration newCollectionDuration = Duration.ofSeconds(
                    Arrays.stream(result.routes[0].legs)
                            .mapToLong(leg -> leg.duration.inSeconds)
                            .sum()
            );

            Distance newDistance = Distance.fromMeters(
                    Arrays.stream(result.routes[0].legs)
                            .mapToLong(leg -> leg.distance.inMeters)
                            .sum()
            );

            // Get district config for maxRouteDuration
            DistrictConfigDTO districtConfig = municipalOperationsContextFacade
                    .getDistrictConfiguration(route.getDistrictId().value())
                    .orElseThrow();

            Duration totalDuration = newCollectionDuration.plus(route.getReturnDuration() != null
                    ? route.getReturnDuration()
                    : Duration.ZERO);

            // Check if exceeds maxRouteDuration
            if (totalDuration.compareTo(districtConfig.maxRouteDuration()) > 0) {
                log.warn("Route {} exceeds maxRouteDuration after update, removing lowest priority waypoint",
                        route.getId());
                removeLowestPriorityWaypoint(route, remainingWaypoints);
                // Recursively update after removal
                updateRouteEstimates(route);
            } else {
                // Update estimates
                route.updateDurations(newCollectionDuration, route.getReturnDuration() != null
                        ? route.getReturnDuration()
                        : Duration.ZERO);
                route.setTotalDistance(newDistance);
                route.setLastLocationUpdate(LocalDateTime.now());
                routeRepository.save(route);

                log.info("Updated route {} estimates: duration={}, distance={}km",
                        route.getId(), totalDuration.toMinutes(), newDistance.kilometers());
            }

        } catch (Exception e) {
            log.error("Error updating route {} estimates: {}", route.getId(), e.getMessage(), e);
        }
    }

    /**
     * Recalculate route with current traffic
     */
    private DirectionsResult recalculateWithCurrentTraffic(Location currentLocation, List<WayPoint> remainingWaypoints) throws Exception {
        List<LatLng> locations = remainingWaypoints.stream()
                .map(wp -> containerMonitoringFacade.getContainerInfo(wp.getContainerId().value())
                        .map(c -> c.location().toGoogleLatLng())
                        .orElseThrow())
                .toList();

        return DirectionsApi.newRequest(geoApiContext)
                .origin(currentLocation.toGoogleLatLng())
                .waypoints(locations.toArray(new LatLng[0]))
                .destination(locations.getLast())
                .departureTime(Instant.now())
                .trafficModel(TrafficModel.BEST_GUESS) // Current traffic
                .await();
    }

    /**
     * Remove the lowest priority waypoint
     */
    private void removeLowestPriorityWaypoint(Route route, List<WayPoint> remainingWaypoints) {
        WayPoint lowestPriority = remainingWaypoints.stream()
                .filter(wp -> wp.getPriority().level() != PriorityLevel.CRITICAL)
                .min(Comparator.comparing(wp -> wp.getPriority().level()))
                .orElse(null);

        if (lowestPriority == null) {
            log.warn("All waypoints are CRITICAL, cannot remove any");
            return;
        }

        log.info("Removing waypoint {} (priority: {}) from route {}",
                lowestPriority.getId(), lowestPriority.getPriority().level(), route.getId());

        // Get container info before removal
        var containerInfo = containerMonitoringFacade.getContainerInfo(lowestPriority.getContainerId().value()).orElseThrow();

        // Publish WebSocket notification for route-specific updates
        var waypointRemovedPayload = WaypointPayloadAssembler.toRemovedPayload(
                route.getId(),
                lowestPriority,
                lowestPriority.getContainerId().value(),
                "Route exceeds maxRouteDuration with current traffic, lowest priority waypoint removed"
        );
        routeWebSocketPublisher.publishWaypointRemoved(waypointRemovedPayload);

        route.removeWaypoint(lowestPriority.getId());
        routeRepository.save(route);

        // Publish domain event for a notification system
        RouteWaypointRemovedEvent event = RouteWaypointRemovedEvent.builder()
                .source(this)
                .routeId(route.getId())
                .driverId(route.getDriverId() != null ? route.getDriverId().value() : null)
                .districtId(route.getDistrictId().value())
                .waypointId(lowestPriority.getId())
                .containerId(containerInfo.containerId())
                .containerLocation(containerInfo.location())
                .priority(lowestPriority.getPriority().level().name())
                .reason("Route exceeds maxRouteDuration with current traffic, lowest priority waypoint removed")
                .build();
        eventPublisher.publishEvent(event);
    }

    /**
     * Update route when waypoint is marked as VISITED
     */
    public void onWaypointVisited(Route route) {
        log.info("Waypoint visited in route {}, updating estimates", route.getId());
        updateRouteEstimates(route);
    }
}
