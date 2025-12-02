package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.websocket.NotificationWebSocketPublisher;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateNotificationCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationChannel;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationPriority;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationType;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.NotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events.RouteWaypointRemovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class RouteWaypointRemovedEventHandler {
    private final NotificationCommandService notificationCommandService;
    private final NotificationWebSocketPublisher webSocketPublisher;

    @EventListener
    public void on(RouteWaypointRemovedEvent event) {
        log.info("Received RouteWaypointRemovedEvent for route: {}, driver: {}",
                event.getRouteId(), event.getDriverId());

        if (event.getDriverId() == null) {
            log.warn("No driver assigned to route {}, skipping notification", event.getRouteId());
            return;
        }

        CreateNotificationCommand command = new CreateNotificationCommand(
                event.getDriverId(),
                NotificationType.WAYPOINT_REMOVED.name(),
                "Waypoint Removed",
                String.format("Waypoint (container %s, priority: %s) has been removed from your route. Reason: %s", event.getContainerId(), event.getPriority(), event.getReason()),
                NotificationPriority.NORMAL.name(),
                NotificationChannel.WEBSOCKET.name(),
                event.getRouteId()
        );

        var notification = notificationCommandService.handle(command);

        if (notification.isEmpty()) {
            log.warn("Notification creation failed for driver: {}", event.getDriverId());
            return;
        }

        NotificationWebSocketPublisher.DriverNotificationPayload payload =
                NotificationWebSocketPublisher.DriverNotificationPayload.builder()
                        .notificationId(notification.get().getId())
                        .routeId(event.getRouteId())
                        .driverId(event.getDriverId())
                        .type(NotificationType.WAYPOINT_REMOVED.name())
                        .title(notification.get().getTitle())
                        .message(notification.get().getMessage())
                        .priority(NotificationPriority.NORMAL.name())
                        .timestamp(event.getOccurredAt())
                        .build();

        webSocketPublisher.publishToUser(event.getDriverId(), payload);

        log.info("Notification created and published for driver: {}", event.getDriverId());
    }
}
