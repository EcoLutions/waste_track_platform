package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllContainersByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllContainersQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetContainerByIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.ContainerCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries.ContainerQueryService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.CreateContainerResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.UpdateContainerResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.ContainerResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromentitytoresponse.ContainerResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromresourcetocommand.CreateContainerCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromresourcetocommand.UpdateContainerCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.swagger.ContainerController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContainerControllerImpl implements ContainerController {
    private final ContainerCommandService containerCommandService;
    private final ContainerQueryService containerQueryService;

    @Override
    public ResponseEntity<ContainerResource> createContainer(CreateContainerResource resource) {
        var command = CreateContainerCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdContainer = containerCommandService.handle(command);
        if (createdContainer.isEmpty()) return ResponseEntity.badRequest().build();
        var containerResource = ContainerResourceFromEntityAssembler.toResourceFromEntity(createdContainer.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(containerResource.id())
                .toUri();
        return ResponseEntity.created(location).body(containerResource);
    }

    @Override
    public ResponseEntity<ContainerResource> updateContainer(String id, UpdateContainerResource resource) {
        var command = UpdateContainerCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedContainer = containerCommandService.handle(command);
        if (updatedContainer.isEmpty()) return ResponseEntity.notFound().build();
        var containerResource = ContainerResourceFromEntityAssembler.toResourceFromEntity(updatedContainer.get());
        return ResponseEntity.ok(containerResource);
    }

    @Override
    public ResponseEntity<Void> deleteContainer(String id) {
        var command = new DeleteContainerCommand(id);
        var result = containerCommandService.handle(command);
        if (!result) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ContainerResource> getContainerById(String id) {
        var query = new GetContainerByIdQuery(id);
        var container = containerQueryService.handle(query);
        if (container.isEmpty()) return ResponseEntity.notFound().build();
        var containerResource = ContainerResourceFromEntityAssembler.toResourceFromEntity(container.get());
        return ResponseEntity.ok(containerResource);
    }

    @Override
    public ResponseEntity<List<ContainerResource>> getAllContainers() {
        var query = new GetAllContainersQuery();
        var containers = containerQueryService.handle(query);
        var containerResources = containers.stream()
                .map(ContainerResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(containerResources);
    }

    @Override
    public ResponseEntity<List<ContainerResource>> getAllContainersByDistrictId(String districtId) {
        var query = new GetAllContainersByDistrictIdQuery(districtId);
        var containers = containerQueryService.handle(query);
        var containerResources = containers.stream()
                .map(ContainerResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(containerResources);
    }
}