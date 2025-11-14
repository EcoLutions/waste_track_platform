package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Payload for waypoint added notification via WebSocket
 * Published to: /topic/routes/{routeId}/waypoints
 */
@Builder
public record WaypointAddedPayload(
        String routeId,
        String waypointId,
        String containerId,
        Integer sequenceOrder,
        String priority,
        String latitude,
        String longitude,
        String action, // "ADDED"
        String reason, // "Container became CRITICAL"
        LocalDateTime timestamp
) {
}
