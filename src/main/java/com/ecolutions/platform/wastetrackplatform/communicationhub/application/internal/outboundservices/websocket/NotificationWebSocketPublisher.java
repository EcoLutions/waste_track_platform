package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.websocket;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationWebSocketPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public void publishToUser(String userId, DriverNotificationPayload payload) {
        String destination = "/topic/user/" + userId + "/driver/notifications";
        log.info("Publishing notification to user {} at destination: {}", userId, destination);
        messagingTemplate.convertAndSend(destination, payload);
    }

    public void publishCriticalAlert(CriticalAlertPayload payload) {
        String destination = "/topic/district/" + payload.districtId() + "/critical-alerts";
        log.warn("Publishing critical alert to admins at destination: {}", destination);
        messagingTemplate.convertAndSend(destination, payload);
    }

    @Builder
    public record DriverNotificationPayload(
            String notificationId,
            String routeId,
            String driverId,
            String type,
            String title,
            String message,
            String priority,
            LocalDateTime timestamp
    ) {}

    @Builder
    public record CriticalAlertPayload(
            String routeId,
            String districtId,
            String containerId,
            String containerLocation,
            Integer fillLevel,
            String alertType,
            String message,
            LocalDateTime timestamp
    ) {}
}
