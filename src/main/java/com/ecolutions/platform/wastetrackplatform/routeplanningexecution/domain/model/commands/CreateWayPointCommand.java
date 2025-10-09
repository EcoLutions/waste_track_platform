package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

import java.time.LocalDateTime;

public record CreateWayPointCommand(
    String routeId,
    String containerId,
    Integer sequenceOrder,
    String priority,
    LocalDateTime estimatedArrivalTime,
    String driverNote
) {
    public CreateWayPointCommand {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
        if (sequenceOrder == null || sequenceOrder < 0) {
            throw new IllegalArgumentException("Sequence order cannot be null or negative");
        }
        if (priority == null || priority.isBlank()) {
            throw new IllegalArgumentException("Priority cannot be null or blank");
        }
    }
}