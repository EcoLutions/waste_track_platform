package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;


public record UpdateCurrentLocationRouteCommand(String routeId, String latitude, String longitude) {
    public UpdateCurrentLocationRouteCommand {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
        if (latitude == null || latitude.isBlank()) {
            throw new IllegalArgumentException("Latitude cannot be null or blank");
        }
        if (longitude == null || longitude.isBlank()) {
            throw new IllegalArgumentException("Longitude cannot be null or blank");
        }
    }
}
