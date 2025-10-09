package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.Priority;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.PriorityLevel;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.WayPointCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.WayPointRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
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
        try {
            Route existingRoute = routeRepository.findById(command.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + command.routeId() + " not found."));

            Priority priority = new Priority(PriorityLevel.fromString(command.priority()));
            ContainerId containerId = ContainerId.of(command.containerId());

            WayPoint wayPoint = new WayPoint(containerId, command.sequenceOrder(), priority);
            wayPoint.setEstimatedArrivalTime(command.estimatedArrivalTime());
            wayPoint.setDriverNote(command.driverNote());

            existingRoute.addWayPoint(wayPoint);

            Route savedRoute = routeRepository.save(existingRoute);

            WayPoint savedWayPoint = savedRoute.getWaypoints().stream()
                .filter(wp -> wp.getContainerId().equals(containerId) && Objects.equals(wp.getSequenceOrder(), command.sequenceOrder()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to retrieve the newly created waypoint."));

            return Optional.of(savedWayPoint);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create waypoint: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<WayPoint> handle(UpdateWayPointCommand command) {
        try {
            WayPoint existingWayPoint = wayPointRepository.findById(command.wayPointId())
                .orElseThrow(() -> new IllegalArgumentException("WayPoint with ID " + command.wayPointId() + " not found."));

            if (command.sequenceOrder() != null) {
                existingWayPoint.setSequenceOrder(command.sequenceOrder());
            }
            if (command.priority() != null) {
                Priority priority = new Priority(PriorityLevel.fromString(command.priority()));
                existingWayPoint.setPriority(priority);
            }
            if (command.estimatedArrivalTime() != null) {
                existingWayPoint.setEstimatedArrivalTime(command.estimatedArrivalTime());
            }
            if (command.driverNote() != null) {
                existingWayPoint.setDriverNote(command.driverNote());
            }

            WayPoint updatedWayPoint = wayPointRepository.save(existingWayPoint);
            return Optional.of(updatedWayPoint);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update waypoint: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteWayPointCommand command) {
        try {
            WayPoint existingWayPoint = wayPointRepository.findById(command.wayPointId())
                .orElseThrow(() -> new IllegalArgumentException("WayPoint with ID " + command.wayPointId() + " not found."));
            wayPointRepository.delete(existingWayPoint);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete waypoint: " + e.getMessage(), e);
        }
    }
}