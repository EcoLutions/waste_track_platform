package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request;

import java.time.LocalDate;

public record UpdateRouteResource(
    String routeId,
    String districtId,
    String routeType,
    LocalDate scheduledDate
) {
    public UpdateRouteResource {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
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