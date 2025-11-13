package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response;

import com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.dto.response.DistanceResource;
import com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.dto.response.DurationResource;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RouteResource(
    String id,
    String districtId,
    String vehicleId,
    String driverId,
    String routeType,
    String status,
    String scheduledDate,
    String startedAt,
    String completedAt,
    DistanceResource totalDistance,
    DurationResource estimatedDuration,
    DurationResource actualDuration,
    String currentLatitude,
    String currentLongitude,
    String lastLocationUpdate,
    String createdAt,
    String updatedAt
) {
}