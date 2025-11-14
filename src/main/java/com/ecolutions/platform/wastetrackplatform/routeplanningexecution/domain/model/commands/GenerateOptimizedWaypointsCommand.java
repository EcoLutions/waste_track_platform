package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

public record GenerateOptimizedWaypointsCommand(
        String routeId
) {
    public GenerateOptimizedWaypointsCommand {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
    }
}
