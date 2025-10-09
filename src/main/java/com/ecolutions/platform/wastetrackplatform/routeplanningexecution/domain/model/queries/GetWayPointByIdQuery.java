package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries;

public record GetWayPointByIdQuery(String wayPointId) {
    public GetWayPointByIdQuery {
        if (wayPointId == null || wayPointId.isBlank()) {
            throw new IllegalArgumentException("WayPoint ID cannot be null or blank");
        }
    }
}