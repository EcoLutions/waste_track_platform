package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.websocket.NotificationWebSocketPublisher;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationType;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events.CriticalContainerNotAddedEvent;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CriticalContainerNotAddedEventHandler {
    private final NotificationWebSocketPublisher webSocketPublisher;

    @EventListener
    public void on(CriticalContainerNotAddedEvent event) {
        log.warn("Received CriticalContainerNotAddedEvent for container: {}, district: {}",
                event.getContainerId(), event.getDistrictId());

        NotificationWebSocketPublisher.CriticalAlertPayload payload =
                NotificationWebSocketPublisher.CriticalAlertPayload.builder()
                        .routeId(event.getRouteId())
                        .districtId(event.getDistrictId())
                        .containerId(event.getContainerId())
                        .containerLocation(formatLocation(event.getContainerLocation()))
                        .fillLevel(event.getFillLevel())
                        .alertType(NotificationType.CRITICAL_CONTAINER_NOT_ADDED.name())
                        .message(String.format("Critical container %s (fill level: %d%%) could not be added to route %s. Reason: %s", event.getContainerId(), event.getFillLevel(), event.getRouteId(), event.getReason()))
                        .timestamp(event.getOccurredAt())
                        .build();

        webSocketPublisher.publishCriticalAlert(payload);

        log.warn("Critical alert published for container: {}", event.getContainerId());
    }

    private String formatLocation(Location location) {
        if (location == null) {
            return "Unknown";
        }
        return String.format("%.6f,%.6f", location.latitude(), location.longitude());
    }
}
