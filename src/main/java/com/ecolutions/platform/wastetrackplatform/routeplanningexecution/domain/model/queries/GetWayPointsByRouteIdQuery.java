package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries;

public record GetWayPointsByRouteIdQuery(String routeId) {
    public GetWayPointsByRouteIdQuery {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
    }
}