package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries;

import lombok.Builder;

import java.util.List;

@Builder
public record GetAllRoutesQuery(
        String districtId,
        String driverId,
        String vehicleId,
        String status,
        List<String> statuses
) {
}