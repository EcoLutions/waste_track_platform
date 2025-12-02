package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Payload for route current location updates via WebSocket
 * Published to: /topic/routes/{routeId}/location
 */
@Builder
public record RouteCurrentLocationUpdatedPayload(
        String routeId,
        String latitude,
        String longitude,
        LocalDateTime timestamp,
        String status,
        Integer remainingWaypoints,
        String estimatedCompletionTime
) {
}
