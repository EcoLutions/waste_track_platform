package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.MarkWayPointAsVisitedCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.WayPointCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.WayPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WayPointCommandServiceImpl implements WayPointCommandService {
    private final RouteRepository routeRepository;
    private final WayPointRepository wayPointRepository;

    @Override
    public Optional<WayPoint> handle(CreateWayPointCommand command) {
        Route existingRoute = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));
        WayPoint wayPoint = new WayPoint(command);
        existingRoute.addWayPoint(wayPoint);
        Route savedRoute = routeRepository.save(existingRoute);
        WayPoint savedWayPoint = savedRoute.getWaypoints().stream()
                .filter(wp -> wp.getContainerId().equals(wayPoint.getContainerId()) && Objects.equals(wp.getSequenceOrder(), command.sequenceOrder()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to retrieve the newly created waypoint."));
        return Optional.of(savedWayPoint);
    }

    @Override
    public Optional<WayPoint> handle(UpdateWayPointCommand command) {
        WayPoint existingWayPoint = wayPointRepository.findById(command.wayPointId())
                .orElseThrow(() -> new IllegalArgumentException("WayPoint with ID " + command.wayPointId() + " not found."));
        existingWayPoint.update(command);
        WayPoint updatedWayPoint = wayPointRepository.save(existingWayPoint);
        return Optional.of(updatedWayPoint);
    }

    @Override
    public Boolean handle(DeleteWayPointCommand command) {
        WayPoint existingWayPoint = wayPointRepository.findById(command.wayPointId())
                .orElseThrow(() -> new IllegalArgumentException("WayPoint with ID " + command.wayPointId() + " not found."));
        wayPointRepository.delete(existingWayPoint);
        return true;
    }

    @Override
    public Optional<WayPoint> handle(MarkWayPointAsVisitedCommand command) {
        WayPoint existingWayPoint = wayPointRepository.findById(command.waypointId())
                .orElseThrow(() -> new IllegalArgumentException("WayPoint with ID " + command.waypointId() + " not found."));
        existingWayPoint.markAsVisited(command.arrivalTime());
        WayPoint updatedWayPoint = wayPointRepository.save(existingWayPoint);
        return Optional.of(updatedWayPoint);
    }
}