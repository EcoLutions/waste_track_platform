package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

import java.time.LocalDateTime;

public record UpdateWayPointCommand(
    String wayPointId,
    Integer sequenceOrder,
    String priority,
    LocalDateTime estimatedArrivalTime,
    String driverNote
) {
    public UpdateWayPointCommand {
        if (wayPointId == null || wayPointId.isBlank()) {
            throw new IllegalArgumentException("WayPoint ID cannot be null or blank");
        }
        if (sequenceOrder != null && sequenceOrder < 0) {
            throw new IllegalArgumentException("Sequence order cannot be negative");
        }
    }
}