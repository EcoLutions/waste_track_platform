package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts.MunicipalOperationsContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.optimization.OptimizedRouteResult;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.optimization.RouteOptimizationService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.update.RouteUpdateService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.RouteWebSocketPublisherService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.transform.RouteCurrentLocationPayloadAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.GenerateOptimizedWaypointsCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.MarkWayPointAsVisitedCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateCurrentLocationRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.RouteCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteCommandServiceImpl implements RouteCommandService {
    private final RouteRepository routeRepository;
    private final MunicipalOperationsContextFacade municipalOperationsContextFacade;
    private final RouteUpdateService routeUpdateService;
    private final RouteOptimizationService routeOptimizationService;
    private final RouteWebSocketPublisherService routeWebSocketPublisher;

    @Override
    public Optional<Route> handle(CreateRouteCommand command) {
        log.debug("1. Fetching district configuration for districtId={}", command.districtId());
        DistrictConfigDTO districtConfig = municipalOperationsContextFacade.getDistrictConfiguration(command.districtId())
                .orElseThrow(() -> new IllegalArgumentException("District not found or not configured: " + command.districtId()));

        log.debug("2. Validating scheduled time {} is within operating hours", command.scheduledDate().toLocalTime());
        if (!municipalOperationsContextFacade.validateScheduledTime(command.districtId(), command.scheduledDate())) {
            throw new IllegalArgumentException("Scheduled start time " + command.scheduledDate().toLocalTime() + " is outside district operating hours (" + districtConfig.operationStartTime() + " - " + districtConfig.operationEndTime() + ")");
        }

        log.debug("3. Calculating scheduledEndAt = scheduledStartAt + maxRouteDuration - buffer");
        Duration bufferTime = Duration.ofMinutes(30); // Safety buffer
        Duration effectiveMaxDuration = districtConfig.maxRouteDuration().minus(bufferTime);
        LocalDateTime scheduledEndAt = command.scheduledDate().plus(effectiveMaxDuration);

        log.debug("4. Creating new route");
        var newRoute = new Route(command);
        newRoute.setScheduledEndAt(scheduledEndAt);

        var savedRoute = routeRepository.save(newRoute);
        return Optional.of(savedRoute);
    }

    @Override
    public Optional<Route> handle(UpdateRouteCommand command) {
        Route existingRoute = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

        existingRoute.update(command);
        var updatedRoute = routeRepository.save(existingRoute);
        return Optional.of(updatedRoute);
    }

    @Override
    public Boolean handle(DeleteRouteCommand command) {
        var existingRoute = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));
        routeRepository.delete(existingRoute);
        return true;
    }

    @Override
    public Optional<Route> handle(MarkWayPointAsVisitedCommand command) {
        Route route = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));
        route.markWaypointAsVisited(command.waypointId(), command.arrivalTime());
        var updatedRoute = routeRepository.save(route);
        routeUpdateService.onWaypointVisited(updatedRoute);
        return Optional.of(updatedRoute);
    }

    @Override
    @Transactional
    public Optional<Route> handle(GenerateOptimizedWaypointsCommand command) {
        log.info("Generating optimized waypoints for route {}", command.routeId());

        Route route = getRouteById(command.routeId());

        log.info("2. Validate route can be modified");
        if (!route.canBeModified()) {
            throw new IllegalStateException("Cannot generate waypoints for route in status: " + route.getStatus());
        }

        log.info("3. Clear existing waypoints");
        route.getWaypoints().clear();

        log.info("4. Generating optimized waypoints using RouteOptimizationService for district {}", route.getDistrictId());
        OptimizedRouteResult result = routeOptimizationService.optimizeRoute(
                route.getDistrictId().value(),
                route.getScheduledStartAt()
        );

        log.info("5. Adding {} optimized waypoints to route {}", result.waypoints().size(), route.getId());
        result.waypoints().forEach(route::addWayPoint);

        log.info("6. Updating route estimates");
        route.updateDurations(result.collectionDuration(), result.returnDuration());
        route.setTotalDistance(result.totalDistance());

        log.info("7. Saving updated route");
        Route savedRoute = routeRepository.save(route);

        log.info("Generated {} optimized waypoints for route {}, total duration: {} minutes", result.waypoints().size(), route.getId(), result.totalDuration().toMinutes());
        return Optional.of(savedRoute);
    }

    @Override
    @Transactional
    public Optional<Route> handle(UpdateCurrentLocationRouteCommand command) {
        log.info("Updating current location for route {}", command.routeId());

        Route route = getRouteById(command.routeId());

        log.info("2. Validate route is IN_PROGRESS");
        if (route.getStatus() != RouteStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot update location for route in status: " + route.getStatus());
        }

        log.info("3. Update current location to ({}, {})", command.latitude(), command.longitude());
        Location newLocation = Location.fromBigDecimal(
                new java.math.BigDecimal(command.latitude()),
                new java.math.BigDecimal(command.longitude())
        );
        route.updateCurrentLocation(newLocation);
        route.setLastLocationUpdate(LocalDateTime.now());

        log.info("4. Save updated route location");
        Route updatedRoute = routeRepository.save(route);

        log.info("5. Publish route location update via WebSocket");
        var locationPayload = RouteCurrentLocationPayloadAssembler.toPayload(updatedRoute);
        routeWebSocketPublisher.publishRouteLocationUpdate(locationPayload);

        log.info("Updated current location for route {} to ({}, {})",
                route.getId(), command.latitude(), command.longitude());

        return Optional.of(updatedRoute);
    }

    private Route getRouteById(String routeId) {
        log.debug("1. Fetching route with id {}", routeId);
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + routeId + " not found."));
    }
}