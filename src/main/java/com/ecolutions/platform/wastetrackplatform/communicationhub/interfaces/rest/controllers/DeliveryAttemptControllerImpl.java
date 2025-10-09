package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.DeleteDeliveryAttemptCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetDeliveryAttemptByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetAllDeliveryAttemptsQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.DeliveryAttemptCommandService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries.DeliveryAttemptQueryService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.CreateDeliveryAttemptResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.UpdateDeliveryAttemptResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.DeliveryAttemptResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand.CreateDeliveryAttemptCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromentitytoresponse.DeliveryAttemptResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand.UpdateDeliveryAttemptCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.swagger.DeliveryAttemptController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/delivery-attempts", produces = "application/json")
@RequiredArgsConstructor
public class DeliveryAttemptControllerImpl implements DeliveryAttemptController {
    private final DeliveryAttemptCommandService deliveryAttemptCommandService;
    private final DeliveryAttemptQueryService deliveryAttemptQueryService;

    @Override
    @PostMapping()
    public ResponseEntity<DeliveryAttemptResource> createDeliveryAttempt(CreateDeliveryAttemptResource resource) {
        var command = CreateDeliveryAttemptCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdDeliveryAttempt = deliveryAttemptCommandService.handle(command);
        if (createdDeliveryAttempt.isEmpty()) return ResponseEntity.badRequest().build();
        var deliveryAttemptResource = DeliveryAttemptResourceFromEntityAssembler.toResourceFromEntity(createdDeliveryAttempt.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(deliveryAttemptResource.id())
                .toUri();
        return ResponseEntity.created(location).body(deliveryAttemptResource);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryAttemptResource> getDeliveryAttemptById(String id) {
        var query = new GetDeliveryAttemptByIdQuery(id);
        var deliveryAttempt = deliveryAttemptQueryService.handle(query);
        if (deliveryAttempt.isEmpty()) return ResponseEntity.notFound().build();
        var deliveryAttemptResource = DeliveryAttemptResourceFromEntityAssembler.toResourceFromEntity(deliveryAttempt.get());
        return ResponseEntity.status(HttpStatus.OK).body(deliveryAttemptResource);
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<DeliveryAttemptResource>> getAllDeliveryAttempts() {
        var query = new GetAllDeliveryAttemptsQuery();
        var deliveryAttempts = deliveryAttemptQueryService.handle(query);
        var deliveryAttemptResources = deliveryAttempts.stream()
                .map(DeliveryAttemptResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(deliveryAttemptResources);
    }

    @Override
    @PutMapping()
    public ResponseEntity<DeliveryAttemptResource> updateDeliveryAttempt(UpdateDeliveryAttemptResource resource) {
        var command = UpdateDeliveryAttemptCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedDeliveryAttempt = deliveryAttemptCommandService.handle(command);
        if (updatedDeliveryAttempt.isEmpty()) return ResponseEntity.badRequest().build();
        var deliveryAttemptResource = DeliveryAttemptResourceFromEntityAssembler.toResourceFromEntity(updatedDeliveryAttempt.get());
        return ResponseEntity.status(HttpStatus.OK).body(deliveryAttemptResource);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryAttempt(String id) {
        var command = new DeleteDeliveryAttemptCommand(id);
        var deleted = deliveryAttemptCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}