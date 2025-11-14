package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Payload for waypoint-removed notification via WebSocket
 * Published to: /topic/routes/{routeId}/waypoints
 */
@Builder
public record WaypointRemovedPayload(
        String routeId,
        String waypointId,
        String containerId,
        String action, // "REMOVED"
        String reason, // "Route exceeds maxRouteDuration with current traffic"
        LocalDateTime timestamp
) {
}
