package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request;

import java.time.LocalDateTime;

public record CreateWayPointResource(
    String containerId,
    Integer sequenceOrder,
    String priority,
    LocalDateTime estimatedArrivalTime,
    String driverNote
) {
    public CreateWayPointResource {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
        if (sequenceOrder == null || sequenceOrder < 0) {
            throw new IllegalArgumentException("Sequence order cannot be null or negative");
        }
        if (priority == null || priority.isBlank()) {
            throw new IllegalArgumentException("Priority cannot be null or blank");
        }
    }
}