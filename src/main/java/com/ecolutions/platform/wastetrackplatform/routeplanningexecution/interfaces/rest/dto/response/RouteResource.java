package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record RouteResource(
    String id,
    String districtId,
    String vehicleId,
    String driverId,
    String status,
    String scheduledStartAt,
    String scheduledEndAt,
    String startedAt,
    String completedAt,
    String totalDistance,
    String estimatedDuration,
    String collectionDuration,
    String returnDuration,
    String actualDuration,
    String currentLatitude,
    String currentLongitude,
    String lastLocationUpdate,
    String createdAt,
    String updatedAt
) {
}