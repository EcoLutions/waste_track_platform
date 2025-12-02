package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Payload for waypoint replaced notification via WebSocket
 * Published to: /topic/routes/{routeId}/waypoints
 */
@Builder
public record WaypointReplacedPayload(
        String routeId,
        String removedWaypointId,
        String removedContainerId,
        String addedWaypointId,
        String addedContainerId,
        Integer sequenceOrder,
        String priority,
        String latitude,
        String longitude,
        String action, // "REPLACED"
        String reason, // "Replaced by CRITICAL container"
        LocalDateTime timestamp
) {
}
