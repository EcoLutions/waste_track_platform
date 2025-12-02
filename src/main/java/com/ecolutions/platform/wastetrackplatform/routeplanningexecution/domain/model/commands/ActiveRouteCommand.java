package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

public record ActiveRouteCommand(String routeId) {
    public ActiveRouteCommand {
        if (routeId == null || routeId.isBlank())
            throw new IllegalArgumentException("Route ID cannot be null or blank");
    }
}
