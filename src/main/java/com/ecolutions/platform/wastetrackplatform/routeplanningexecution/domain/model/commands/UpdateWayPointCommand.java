package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

import java.time.LocalDateTime;

public record UpdateWayPointCommand(
    String wayPointId,
    Integer sequenceOrder,
    String priority,
    LocalDateTime estimatedArrivalTime
) {
    public UpdateWayPointCommand {
        if (wayPointId == null || wayPointId.isBlank()) {
            throw new IllegalArgumentException("WayPoint ID cannot be null or blank");
        }
    }
}