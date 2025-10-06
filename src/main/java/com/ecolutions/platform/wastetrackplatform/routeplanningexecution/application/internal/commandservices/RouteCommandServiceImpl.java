package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteType;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.RouteCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteCommandServiceImpl implements RouteCommandService {
    private final RouteRepository routeRepository;

    @Override
    public Optional<Route> handle(CreateRouteCommand command) {
        try {
            var districtId = DistrictId.of(command.districtId());
            var routeType = RouteType.fromString(command.routeType());
            var newRoute = new Route(districtId, routeType, command.scheduledDate());

            var savedRoute = routeRepository.save(newRoute);

            return Optional.of(savedRoute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create route: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Route> handle(UpdateRouteCommand command) {
        try {
            Route existingRoute = routeRepository.findById(command.routeId())
                    .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

            if (command.districtId() != null && !command.districtId().isBlank()) {
                var districtId = DistrictId.of(command.districtId());
                existingRoute.setDistrictId(districtId);
            }

            if (command.routeType() != null && !command.routeType().isBlank()) {
                var routeType = RouteType.fromString(command.routeType());
                existingRoute.setRouteType(routeType);
            }

            if (command.scheduledDate() != null) {
                existingRoute.setScheduledDate(command.scheduledDate());
            }

            var updatedRoute = routeRepository.save(existingRoute);
            return Optional.of(updatedRoute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update route: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteRouteCommand command) {
        try {
            var existingRoute = routeRepository.findById(command.routeId())
                    .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

            routeRepository.delete(existingRoute);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete route: " + e.getMessage(), e);
        }
    }
}