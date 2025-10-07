package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.DeleteCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllCitizensQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetCitizenByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command.CitizenCommandService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries.CitizenQueryService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.CreateCitizenResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.UpdateCitizenResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response.CitizenResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromentitytoresponse.CitizenResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand.CreateCitizenCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand.UpdateCitizenCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.swagger.CitizenController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CitizenControllerImpl implements CitizenController {
    private final CitizenCommandService citizenCommandService;
    private final CitizenQueryService citizenQueryService;

    @Override
    public ResponseEntity<CitizenResource> createCitizen(CreateCitizenResource resource) {
        var command = CreateCitizenCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdCitizen = citizenCommandService.handle(command);
        if (createdCitizen.isEmpty()) return ResponseEntity.badRequest().build();
        var citizenResource = CitizenResourceFromEntityAssembler.toResourceFromEntity(createdCitizen.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(citizenResource.id())
                .toUri();
        return ResponseEntity.created(location).body(citizenResource);
    }

    @Override
    public ResponseEntity<CitizenResource> getCitizenById(String id) {
        var query = new GetCitizenByIdQuery(id);
        var citizen = citizenQueryService.handle(query);
        if (citizen.isEmpty()) return ResponseEntity.notFound().build();
        var citizenResource = CitizenResourceFromEntityAssembler.toResourceFromEntity(citizen.get());
        return ResponseEntity.status(HttpStatus.OK).body(citizenResource);
    }

    @Override
    public ResponseEntity<List<CitizenResource>> getAllCitizens() {
        var query = new GetAllCitizensQuery();
        var citizens = citizenQueryService.handle(query);
        var citizenResources = citizens.stream()
                .map(CitizenResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(citizenResources);
    }

    @Override
    public ResponseEntity<CitizenResource> updateCitizen(String id, UpdateCitizenResource resource) {
        var command = UpdateCitizenCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedCitizen = citizenCommandService.handle(command);
        if (updatedCitizen.isEmpty()) return ResponseEntity.badRequest().build();
        var citizenResource = CitizenResourceFromEntityAssembler.toResourceFromEntity(updatedCitizen.get());
        return ResponseEntity.status(HttpStatus.OK).body(citizenResource);
    }

    @Override
    public ResponseEntity<Void> deleteCitizen(String id) {
        var command = new DeleteCitizenCommand(id);
        var deleted = citizenCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}