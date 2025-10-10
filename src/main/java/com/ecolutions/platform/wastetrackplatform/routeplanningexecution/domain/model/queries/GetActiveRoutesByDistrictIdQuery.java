package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries;

public record GetActiveRoutesByDistrictIdQuery(String districtId) {
    public GetActiveRoutesByDistrictIdQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
    }
}