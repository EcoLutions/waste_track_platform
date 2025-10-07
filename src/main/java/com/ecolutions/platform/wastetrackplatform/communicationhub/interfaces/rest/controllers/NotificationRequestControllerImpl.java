package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.DeleteNotificationRequestCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetAllNotificationRequestsQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetNotificationRequestByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.NotificationRequestCommandService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries.NotificationRequestQueryService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.CreateNotificationRequestResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.UpdateNotificationRequestResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.NotificationRequestResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromentitytoresponse.NotificationRequestResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand.CreateNotificationRequestCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand.UpdateNotificationRequestCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.swagger.NotificationRequestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationRequestControllerImpl implements NotificationRequestController {
    private final NotificationRequestCommandService notificationRequestCommandService;
    private final NotificationRequestQueryService notificationRequestQueryService;

    @Override
    public ResponseEntity<NotificationRequestResource> createNotificationRequest(CreateNotificationRequestResource resource) {
        var command = CreateNotificationRequestCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdNotificationRequest = notificationRequestCommandService.handle(command);
        if (createdNotificationRequest.isEmpty()) return ResponseEntity.badRequest().build();
        var notificationRequestResource = NotificationRequestResourceFromEntityAssembler.toResourceFromEntity(createdNotificationRequest.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(notificationRequestResource.id())
                .toUri();
        return ResponseEntity.created(location).body(notificationRequestResource);
    }

    @Override
    public ResponseEntity<NotificationRequestResource> getNotificationRequestById(String id) {
        var query = new GetNotificationRequestByIdQuery(id);
        var notificationRequest = notificationRequestQueryService.handle(query);
        if (notificationRequest.isEmpty()) return ResponseEntity.notFound().build();
        var notificationRequestResource = NotificationRequestResourceFromEntityAssembler.toResourceFromEntity(notificationRequest.get());
        return ResponseEntity.ok(notificationRequestResource);
    }

    @Override
    public ResponseEntity<List<NotificationRequestResource>> getAllNotificationRequests() {
        var query = new GetAllNotificationRequestsQuery();
        var notificationRequests = notificationRequestQueryService.handle(query);
        var notificationRequestResources = notificationRequests.stream()
                .map(NotificationRequestResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(notificationRequestResources);
    }

    @Override
    public ResponseEntity<NotificationRequestResource> updateNotificationRequest(String id, UpdateNotificationRequestResource resource) {
        var command = UpdateNotificationRequestCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedNotificationRequest = notificationRequestCommandService.handle(command);
        if (updatedNotificationRequest.isEmpty()) return ResponseEntity.badRequest().build();
        var notificationRequestResource = NotificationRequestResourceFromEntityAssembler.toResourceFromEntity(updatedNotificationRequest.get());
        return ResponseEntity.ok(notificationRequestResource);
    }

    @Override
    public ResponseEntity<Void> deleteNotificationRequest(String id) {
        var command = new DeleteNotificationRequestCommand(id);
        var result = notificationRequestCommandService.handle(command);
        if (!result) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}