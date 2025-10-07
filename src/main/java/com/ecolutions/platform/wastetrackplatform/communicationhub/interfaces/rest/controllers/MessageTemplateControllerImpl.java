package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.DeleteMessageTemplateCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetAllMessageTemplatesQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetMessageTemplateByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.MessageTemplateCommandService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries.MessageTemplateQueryService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.CreateMessageTemplateResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.UpdateMessageTemplateResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.MessageTemplateResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromentitytoresponse.MessageTemplateResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand.CreateMessageTemplateCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand.UpdateMessageTemplateCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.swagger.MessageTemplateController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageTemplateControllerImpl implements MessageTemplateController {
    private final MessageTemplateCommandService messageTemplateCommandService;
    private final MessageTemplateQueryService messageTemplateQueryService;

    @Override
    public ResponseEntity<MessageTemplateResource> createMessageTemplate(CreateMessageTemplateResource resource) {
        var command = CreateMessageTemplateCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdMessageTemplate = messageTemplateCommandService.handle(command);
        if (createdMessageTemplate.isEmpty()) return ResponseEntity.badRequest().build();
        var messageTemplateResource = MessageTemplateResourceFromEntityAssembler.toResourceFromEntity(createdMessageTemplate.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(messageTemplateResource.id())
                .toUri();
        return ResponseEntity.created(location).body(messageTemplateResource);
    }

    @Override
    public ResponseEntity<MessageTemplateResource> getMessageTemplateById(String id) {
        var query = new GetMessageTemplateByIdQuery(id);
        var messageTemplate = messageTemplateQueryService.handle(query);
        if (messageTemplate.isEmpty()) return ResponseEntity.notFound().build();
        var messageTemplateResource = MessageTemplateResourceFromEntityAssembler.toResourceFromEntity(messageTemplate.get());
        return ResponseEntity.status(HttpStatus.OK).body(messageTemplateResource);
    }

    @Override
    public ResponseEntity<List<MessageTemplateResource>> getAllMessageTemplates() {
        var query = new GetAllMessageTemplatesQuery();
        var messageTemplates = messageTemplateQueryService.handle(query);
        var messageTemplateResources = messageTemplates.stream()
                .map(MessageTemplateResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(messageTemplateResources);
    }

    @Override
    public ResponseEntity<MessageTemplateResource> updateMessageTemplate(String id, UpdateMessageTemplateResource resource) {
        var command = UpdateMessageTemplateCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedMessageTemplate = messageTemplateCommandService.handle(command);
        if (updatedMessageTemplate.isEmpty()) return ResponseEntity.badRequest().build();
        var messageTemplateResource = MessageTemplateResourceFromEntityAssembler.toResourceFromEntity(updatedMessageTemplate.get());
        return ResponseEntity.status(HttpStatus.OK).body(messageTemplateResource);
    }

    @Override
    public ResponseEntity<Void> deleteMessageTemplate(String id) {
        var command = new DeleteMessageTemplateCommand(id);
        var deleted = messageTemplateCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}