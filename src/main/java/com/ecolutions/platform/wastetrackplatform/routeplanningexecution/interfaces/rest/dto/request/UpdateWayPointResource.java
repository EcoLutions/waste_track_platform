package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request;

import java.time.LocalDateTime;

public record UpdateWayPointResource(
    Integer sequenceOrder,
    String priority,
    LocalDateTime estimatedArrivalTime
) {
}