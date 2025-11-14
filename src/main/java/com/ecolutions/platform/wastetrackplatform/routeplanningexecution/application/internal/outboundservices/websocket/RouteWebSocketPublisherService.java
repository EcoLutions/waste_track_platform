package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.RouteCurrentLocationUpdatedPayload;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.WaypointAddedPayload;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.WaypointReplacedPayload;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.WaypointRemovedPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RouteWebSocketPublisherService {
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Publish route current location update
     * Destination: /topic/routes/{routeId}/location
     */
    public void publishRouteLocationUpdate(RouteCurrentLocationUpdatedPayload payload) {
        String destination = "/topic/routes/" + payload.routeId() + "/location";
        log.info("Publishing route location update to {}", destination);
        messagingTemplate.convertAndSend(destination, payload);
    }

    /**
     * Publish waypoint added notification
     * Destination: /topic/routes/{routeId}/waypoints
     */
    public void publishWaypointAdded(WaypointAddedPayload payload) {
        String destination = "/topic/routes/" + payload.routeId() + "/waypoints";
        log.info("Publishing waypoint added to route {} at destination {}", payload.routeId(), destination);
        messagingTemplate.convertAndSend(destination, payload);
    }

    /**
     * Publish waypoint-replaced notification
     * Destination: /topic/routes/{routeId}/waypoints
     */
    public void publishWaypointReplaced(WaypointReplacedPayload payload) {
        String destination = "/topic/routes/" + payload.routeId() + "/waypoints";
        log.info("Publishing waypoint replaced on route {} at destination {}", payload.routeId(), destination);
        messagingTemplate.convertAndSend(destination, payload);
    }

    /**
     * Publish waypoint-removed notification
     * Destination: /topic/routes/{routeId}/waypoints
     */
    public void publishWaypointRemoved(WaypointRemovedPayload payload) {
        String destination = "/topic/routes/" + payload.routeId() + "/waypoints";
        log.info("Publishing waypoint removed from route {} at destination {}", payload.routeId(), destination);
        messagingTemplate.convertAndSend(destination, payload);
    }
}
