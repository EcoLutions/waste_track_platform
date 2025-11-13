package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request;

public record CreateWayPointResource(
    String containerId,
    Integer sequenceOrder,
    String priority
) {
    public CreateWayPointResource {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
    }
}