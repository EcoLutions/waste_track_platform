package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request;

public record CreateRouteResource(
    String districtId,
    String routeType,
    String scheduledDate
) {
    public CreateRouteResource {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (routeType == null || routeType.isBlank()) {
            throw new IllegalArgumentException("Route type cannot be null or blank");
        }
        if (scheduledDate == null) {
            throw new IllegalArgumentException("Scheduled date cannot be null");
        }
    }
}