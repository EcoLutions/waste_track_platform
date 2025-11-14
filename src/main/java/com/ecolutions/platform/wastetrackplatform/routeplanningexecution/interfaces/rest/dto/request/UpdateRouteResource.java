package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request;

public record UpdateRouteResource(
    String routeId,
    String scheduledStartAt
) {
    public UpdateRouteResource {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
    }
}