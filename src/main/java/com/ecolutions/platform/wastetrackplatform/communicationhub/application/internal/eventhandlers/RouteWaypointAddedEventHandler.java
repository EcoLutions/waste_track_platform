package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.websocket.NotificationWebSocketPublisher;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateNotificationCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationChannel;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationPriority;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationType;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.NotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events.RouteWaypointAddedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RouteWaypointAddedEventHandler {
    private final NotificationCommandService notificationCommandService;
    private final NotificationWebSocketPublisher webSocketPublisher;

    @EventListener
    public void on(RouteWaypointAddedEvent event) {
        log.info("Received RouteWaypointAddedEvent for route: {}, driver: {}", event.getRouteId(), event.getDriverId());

        if (event.getDriverId() == null) {
            log.warn("No driver assigned to route {}, skipping notification", event.getRouteId());
            return;
        }

        CreateNotificationCommand command = new CreateNotificationCommand(
                event.getDriverId(),
                NotificationType.WAYPOINT_ADDED.name(),
                "New Waypoint Added",
                String.format("A new waypoint (container %s) has been added to your route at position %d. Reason: %s", event.getContainerId(), event.getSequenceOrder(), event.getReason()),
                NotificationPriority.HIGH.name(),
                NotificationChannel.WEBSOCKET.name(),
                event.getRouteId()
        );

        var notification = notificationCommandService.handle(command);


        if (notification.isEmpty()) {
            log.warn("Failed to create notification for driver: {}", event.getDriverId());
            return;
        }

        NotificationWebSocketPublisher.DriverNotificationPayload payload =
                NotificationWebSocketPublisher.DriverNotificationPayload.builder()
                        .notificationId(notification.get().getId())
                        .routeId(event.getRouteId())
                        .driverId(event.getDriverId())
                        .type(NotificationType.WAYPOINT_ADDED.name())
                        .title(notification.get().getTitle())
                        .message(notification.get().getMessage())
                        .priority(NotificationPriority.HIGH.name())
                        .timestamp(event.getOccurredAt())
                        .build();

        webSocketPublisher.publishToUser(event.getDriverId(), payload);

        log.info("Notification created and published for driver: {}", event.getDriverId());
    }
}
