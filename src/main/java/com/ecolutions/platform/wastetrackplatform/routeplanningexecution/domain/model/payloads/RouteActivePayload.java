package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads;

import lombok.Builder;

@Builder
public record RouteActivePayload(
        String routeId,
        String driverId,
        String vehicleId
) {
}
