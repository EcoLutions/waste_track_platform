package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

public record DeleteRouteCommand(String routeId) {
    public DeleteRouteCommand {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
    }
}