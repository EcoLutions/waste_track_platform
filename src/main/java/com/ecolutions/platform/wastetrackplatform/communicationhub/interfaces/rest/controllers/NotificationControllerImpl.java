package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.MarkNotificationAsReadCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetNotificationsByUserIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetUnreadNotificationsQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.NotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries.NotificationQueryService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.NotificationResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromentitytoresponse.NotificationResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.swagger.NotificationController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implementation of NotificationController
 */
@RestController
@RequiredArgsConstructor
public class NotificationControllerImpl implements NotificationController {
    private final NotificationQueryService notificationQueryService;
    private final NotificationCommandService notificationCommandService;

    @Override
    public ResponseEntity<List<NotificationResource>> getNotificationsByUserId(String userId) {
        var query = new GetNotificationsByUserIdQuery(userId);
        var notifications = notificationQueryService.handle(query);
        var resources = notifications.stream()
                .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Override
    public ResponseEntity<List<NotificationResource>> getUnreadNotifications(String userId) {
        var query = new GetUnreadNotificationsQuery(userId);
        var notifications = notificationQueryService.handle(query);
        var resources = notifications.stream()
                .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Override
    public ResponseEntity<NotificationResource> markAsRead(String notificationId) {
        var command = new MarkNotificationAsReadCommand(notificationId);
        return notificationCommandService.handle(command)
                .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
