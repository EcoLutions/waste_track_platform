package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteVehicleCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllVehiclesQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetVehicleByIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command.VehicleCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries.VehicleQueryService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.CreateVehicleResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateVehicleResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.VehicleResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromentitytoresponse.VehicleResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand.CreateVehicleCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand.UpdateVehicleCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.swagger.VehicleController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VehicleControllerImpl implements VehicleController {
    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;

    @Override
    public ResponseEntity<VehicleResource> createVehicle(CreateVehicleResource resource) {
        var command = CreateVehicleCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdVehicle = vehicleCommandService.handle(command);
        if (createdVehicle.isEmpty()) return ResponseEntity.badRequest().build();
        var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(createdVehicle.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vehicleResource.id())
                .toUri();
        return ResponseEntity.created(location).body(vehicleResource);
    }

    @Override
    public ResponseEntity<VehicleResource> updateVehicle(UpdateVehicleResource resource) {
        var command = UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedVehicle = vehicleCommandService.handle(command);
        if (updatedVehicle.isEmpty()) return ResponseEntity.notFound().build();
        var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(updatedVehicle.get());
        return ResponseEntity.status(HttpStatus.OK).body(vehicleResource);
    }

    @Override
    public ResponseEntity<Void> deleteVehicle(String id) {
        var command = new DeleteVehicleCommand(id);
        var deleted = vehicleCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<VehicleResource> getVehicleById(String id) {
        var query = new GetVehicleByIdQuery(id);
        var vehicle = vehicleQueryService.handle(query);
        if (vehicle.isEmpty()) return ResponseEntity.notFound().build();
        var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle.get());
        return ResponseEntity.status(HttpStatus.OK).body(vehicleResource);
    }

    @Override
    public ResponseEntity<List<VehicleResource>> getAllVehicles() {
        var query = new GetAllVehiclesQuery();
        var vehicles = vehicleQueryService.handle(query);
        var vehicleResources = vehicles.stream()
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(vehicleResources);
    }
}