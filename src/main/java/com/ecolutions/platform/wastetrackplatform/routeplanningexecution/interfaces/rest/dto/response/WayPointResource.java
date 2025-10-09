package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record WayPointResource(
    String id,
    String containerId,
    Integer sequenceOrder,
    String priority,
    String status,
    String estimatedArrivalTime,
    String actualArrivalTime,
    String driverNote,
    String createdAt,
    String updatedAt
) {
}