package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDistrictsQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetDistrictByIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command.DistrictCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries.DistrictQueryService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.CreateDistrictResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateDistrictResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.DistrictResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromentitytoresponse.DistrictResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand.CreateDistrictCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand.UpdateDistrictCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.swagger.DistrictController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DistrictControllerImpl implements DistrictController {
    private final DistrictCommandService districtCommandService;
    private final DistrictQueryService districtQueryService;

    @Override
    public ResponseEntity<DistrictResource> createDistrict(CreateDistrictResource resource) {
        var command = CreateDistrictCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdDistrict = districtCommandService.handle(command);
        if (createdDistrict.isEmpty()) return ResponseEntity.badRequest().build();
        var districtResource = DistrictResourceFromEntityAssembler.toResourceFromEntity(createdDistrict.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(districtResource.id())
                .toUri();
        return ResponseEntity.created(location).body(districtResource);
    }

    @Override
    public ResponseEntity<DistrictResource> getDistrictById(String id) {
        var query = new GetDistrictByIdQuery(id);
        var district = districtQueryService.handle(query);
        if (district.isEmpty()) return ResponseEntity.notFound().build();
        var districtResource = DistrictResourceFromEntityAssembler.toResourceFromEntity(district.get());
        return ResponseEntity.status(HttpStatus.OK).body(districtResource);
    }

    @Override
    public ResponseEntity<List<DistrictResource>> getAllDistricts() {
        var query = new GetAllDistrictsQuery();
        var districts = districtQueryService.handle(query);
        var districtResources = districts.stream()
                .map(DistrictResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(districtResources);
    }

    @Override
    public ResponseEntity<DistrictResource> updateDistrict(String id, UpdateDistrictResource resource) {
        var command = UpdateDistrictCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedDistrict = districtCommandService.handle(command);
        if (updatedDistrict.isEmpty()) return ResponseEntity.notFound().build();
        var districtResource = DistrictResourceFromEntityAssembler.toResourceFromEntity(updatedDistrict.get());
        return ResponseEntity.status(HttpStatus.OK).body(districtResource);
    }

    @Override
    public ResponseEntity<Void> deleteDistrict(String id) {
        var command = new DeleteDistrictCommand(id);
        var deleted = districtCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}