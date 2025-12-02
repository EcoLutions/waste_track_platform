package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

public record CancelRouteCommand(String routeId) {
    public CancelRouteCommand {
        if (routeId == null || routeId.isBlank())
            throw new IllegalArgumentException("Route ID cannot be null or blank");
    }
}
