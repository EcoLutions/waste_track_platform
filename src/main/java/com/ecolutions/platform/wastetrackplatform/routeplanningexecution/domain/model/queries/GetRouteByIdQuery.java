package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries;

public record GetRouteByIdQuery(String routeId) {
    public GetRouteByIdQuery {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
    }
}