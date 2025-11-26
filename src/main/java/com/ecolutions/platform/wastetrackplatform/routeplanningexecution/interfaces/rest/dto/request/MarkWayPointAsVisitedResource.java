package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request;

import java.time.LocalDateTime;

public record MarkWayPointAsVisitedResource(
    String routeId,
    LocalDateTime arrivalTime
) {
    public MarkWayPointAsVisitedResource {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
        if (arrivalTime == null) {
            throw new IllegalArgumentException("Arrival time cannot be null");
        }
    }
}

