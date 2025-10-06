package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

public record DeleteWayPointCommand(String wayPointId) {
    public DeleteWayPointCommand {
        if (wayPointId == null || wayPointId.isBlank()) {
            throw new IllegalArgumentException("WayPoint ID cannot be null or blank");
        }
    }
}