package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts.MunicipalOperationsContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.update.RouteUpdateService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.MarkWayPointAsVisitedCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.RouteCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteCommandServiceImpl implements RouteCommandService {
    private final RouteRepository routeRepository;
    private final MunicipalOperationsContextFacade municipalOperationsContextFacade;
    private final RouteUpdateService routeUpdateService;

    @Override
    public Optional<Route> handle(CreateRouteCommand command) {
        // 1. Get district configuration via ACL
        DistrictConfigDTO districtConfig = municipalOperationsContextFacade.getDistrictConfiguration(command.districtId())
                .orElseThrow(() -> new IllegalArgumentException("District not found or not configured: " + command.districtId()));

        // 2. Validate scheduled time is within operating hours
        if (!municipalOperationsContextFacade.validateScheduledTime(command.districtId(), command.scheduledDate())) {
            throw new IllegalArgumentException("Scheduled start time " + command.scheduledDate().toLocalTime() + " is outside district operating hours (" + districtConfig.operationStartTime() + " - " + districtConfig.operationEndTime() + ")");
        }

        // 3. Calculate scheduledEndAt = scheduledStartAt + maxRouteDuration - buffer
        Duration bufferTime = Duration.ofMinutes(30); // Safety buffer
        Duration effectiveMaxDuration = districtConfig.maxRouteDuration().minus(bufferTime);
        LocalDateTime scheduledEndAt = command.scheduledDate().plus(effectiveMaxDuration);

        // 4. Create a route with a calculated scheduledEndAt
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
}