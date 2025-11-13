package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request;

import java.time.LocalDate;

public record UpdateRouteResource(
    String routeId,
    LocalDate scheduledDate
) {
    public UpdateRouteResource {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
    }
}