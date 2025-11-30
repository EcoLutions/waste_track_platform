package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts.MunicipalOperationsContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.optimization.OptimizedRouteResult;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.optimization.RouteOptimizationService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.update.RouteUpdateService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.RouteWebSocketPublisherService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.transform.RouteCurrentLocationPayloadAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.*;
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
            throw new IllegalArgumentException("Scheduled start time " + command.scheduledDate().toLocalTime()
                    + " is outside district operating hours (" + districtConfig.operationStartTime() + " - " + districtConfig.operationEndTime() + ")");
        }

        log.debug("3. Calculating scheduledEndAt = scheduledStartAt + maxRouteDuration - buffer");
        if (districtConfig.maxRouteDuration() == null) {
            throw new IllegalArgumentException("District " + command.districtId() + " does not have maxRouteDuration configured");
        }
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

        Route route = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

        log.info("2. Validate route can be modified");
        if (!route.canBeModified()) {
            throw new IllegalStateException("Cannot generate waypoints for route in status: " + route.getStatus());
        }

        log.info("3. Clear existing waypoints");
        route.getWaypoints().clear();

        log.info("4. Generating optimized waypoints using RouteOptimizationService for district {}", route.getDistrictId());
        OptimizedRouteResult result = routeOptimizationService.optimizeRoute(
                route.getId(),
                route.getDistrictId().value(),
                route.getScheduledStartAt()
        );

        log.info("5. Adding {} optimized waypoints to route {}", result.waypoints().size(), route.getId());
        result.waypoints().forEach(route::addWayPoint);

        log.info("6. Updating route estimates");
        route.updateDurations(result.collectionDuration(), result.returnDuration());
        route.setTotalDistance(result.totalDistance());
        if (result.scheduledEndAt() != null) {
            route.setScheduledEndAt(result.scheduledEndAt());
        }

        log.info("7. Saving updated route");
        Route savedRoute = routeRepository.save(route);

        log.info("Generated {} optimized waypoints for route {}, total duration: {} minutes",
                result.waypoints().size(), route.getId(), result.totalDuration().toMinutes());
        return Optional.of(savedRoute);
    }

    @Override
    @Transactional
    public Optional<Route> handle(UpdateCurrentLocationRouteCommand command) {
        log.info("Updating current location for route {}", command.routeId());

        Route route = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

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

    /**
     * StartRouteCommand:
     * - Si la ruta no tiene waypoints, los genera antes de iniciar.
     * - Cambia estado a IN_PROGRESS y setea startedAt.
     */
    @Override
    @Transactional
    public Optional<Route> handle(StartRouteCommand command) {
        log.info("Starting route {}", command.routeId());

        Route route = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

        if (route.getStatus() != RouteStatus.ASSIGNED) {
            throw new IllegalStateException("Can only start routes in ASSIGNED status. Current status: " + route.getStatus());
        }

        // Si no tiene waypoints, generamos antes de iniciar
        if (route.getWaypoints() == null || route.getWaypoints().isEmpty()) {
            log.info("Route {} has no waypoints, generating optimized waypoints before starting", route.getId());

            OptimizedRouteResult result = routeOptimizationService.optimizeRoute(
                    route.getId(),
                    route.getDistrictId().value(),
                    route.getScheduledStartAt()
            );

            result.waypoints().forEach(route::addWayPoint);
            route.updateDurations(result.collectionDuration(), result.returnDuration());
            route.setTotalDistance(result.totalDistance());
            if (result.scheduledEndAt() != null) {
                route.setScheduledEndAt(result.scheduledEndAt());
            }
        }

        // Cambiar estado a IN_PROGRESS y setear startedAt
        route.startExecution();

        Route savedRoute = routeRepository.save(route);
        log.info("Route {} started at {}", savedRoute.getId(), savedRoute.getStartedAt());

        return Optional.of(savedRoute);
    }

    /**
     * CompleteRouteCommand:
     * - Marca la ruta como COMPLETED.
     * - Calcula actualDuration dentro del agregado.
     */
    @Override
    @Transactional
    public Optional<Route> handle(CompleteRouteCommand command) {
        log.info("Completing route {}", command.routeId());

        Route route = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

        route.completeExecution();

        Route savedRoute = routeRepository.save(route);
        log.info("Route {} completed at {}, actual duration: {} minutes",
                savedRoute.getId(),
                savedRoute.getCompletedAt(),
                savedRoute.getActualDuration() != null ? savedRoute.getActualDuration().toMinutes() : null
        );

        return Optional.of(savedRoute);
    }

    /**
     * CancelRouteCommand:
     * - Cancela la ruta (si no está COMPLETED).
     */
    @Override
    @Transactional
    public Optional<Route> handle(CancelRouteCommand command) {
        log.info("Cancelling route {}", command.routeId());

        Route route = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

        route.cancel();

        Route savedRoute = routeRepository.save(route);
        log.info("Route {} cancelled", savedRoute.getId());

        return Optional.of(savedRoute);
    }

    /**
     * ReOptimizeRouteCommand:
     * - Reoptimiza los waypoints de una ruta ASSIGNED.
     * - Limpia waypoints actuales y recalcula todo.
     */
    @Override
    @Transactional
    public Optional<Route> handle(ReOptimizeRouteCommand command) {
        log.info("Re-optimizing route {}", command.routeId());

        Route route = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

        if (!route.canBeModified()) {
            throw new IllegalStateException("Cannot re-optimize waypoints for route in status: " + route.getStatus());
        }

        log.info("Clearing existing waypoints for route {}", route.getId());
        route.getWaypoints().clear();

        OptimizedRouteResult result = routeOptimizationService.optimizeRoute(
                route.getId(),
                route.getDistrictId().value(),
                route.getScheduledStartAt()
        );

        log.info("Adding {} re-optimized waypoints to route {}", result.waypoints().size(), route.getId());
        result.waypoints().forEach(route::addWayPoint);

        route.updateDurations(result.collectionDuration(), result.returnDuration());
        route.setTotalDistance(result.totalDistance());
        if (result.scheduledEndAt() != null) {
            route.setScheduledEndAt(result.scheduledEndAt());
        }

        Route savedRoute = routeRepository.save(route);
        log.info("Route {} re-optimized. New total duration: {} minutes",
                savedRoute.getId(), result.totalDuration().toMinutes());

        return Optional.of(savedRoute);
    }

    /**
     * UpdateRouteEstimatesCommand:
     * - Permite forzar la actualización de estimaciones (como el scheduler, pero manual).
     */
    @Override
    @Transactional
    public Optional<Route> handle(UpdateRouteEstimatesCommand command) {
        log.info("Updating estimates for route {} (manual trigger)", command.routeId());

        Route route = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

        routeUpdateService.updateRouteEstimates(route);

        Route updatedRoute = routeRepository.findById(command.routeId())
                .orElse(route);

        return Optional.of(updatedRoute);
    }
}
