package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse.UserResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDriversByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDriversQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetCurrentDriverQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetDriverByIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command.DriverCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries.DriverQueryService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.CreateDriverResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateDriverResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.DriverResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromentitytoresponse.DriverResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand.CreateDriverCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand.UpdateDriverCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.swagger.DriverController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DriverControllerImpl implements DriverController {
    private final DriverCommandService driverCommandService;
    private final DriverQueryService driverQueryService;

    @Override
    public ResponseEntity<DriverResource> createDriver(CreateDriverResource resource) {
        var command = CreateDriverCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdDriver = driverCommandService.handle(command);
        if (createdDriver.isEmpty()) return ResponseEntity.badRequest().build();
        var driverResource = DriverResourceFromEntityAssembler.toResourceFromEntity(createdDriver.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(driverResource.id())
                .toUri();
        return ResponseEntity.created(location).body(driverResource);
    }

    @Override
    public ResponseEntity<DriverResource> getDriverById(String id) {
        var query = new GetDriverByIdQuery(id);
        var driver = driverQueryService.handle(query);
        if (driver.isEmpty()) return ResponseEntity.notFound().build();
        var driverResource = DriverResourceFromEntityAssembler.toResourceFromEntity(driver.get());
        return ResponseEntity.status(HttpStatus.OK).body(driverResource);
    }

    @Override
    public ResponseEntity<List<DriverResource>> getAllDrivers() {
        var query = new GetAllDriversQuery();
        var drivers = driverQueryService.handle(query);
        var driverResources = drivers.stream()
                .map(DriverResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(driverResources);
    }

    @Override
    public ResponseEntity<List<DriverResource>> getAllDriversByDistrictId(String districtId) {
        var query = new GetAllDriversByDistrictIdQuery(districtId);
        var drivers = driverQueryService.handle(query);
        var driverResources = drivers.stream()
                .map(DriverResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(driverResources);
    }

    @Override
    public ResponseEntity<DriverResource> updateDriver(UpdateDriverResource resource) {
        var command = UpdateDriverCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedDriver = driverCommandService.handle(command);
        if (updatedDriver.isEmpty()) return ResponseEntity.notFound().build();
        var driverResource = DriverResourceFromEntityAssembler.toResourceFromEntity(updatedDriver.get());
        return ResponseEntity.status(HttpStatus.OK).body(driverResource);
    }

    @Override
    public ResponseEntity<Void> deleteDriver(String id) {
        var command = new DeleteDriverCommand(id);
        var deleted = driverCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<DriverResource> getCurrentDriver() {
        var query = new GetCurrentDriverQuery();
        var driver = driverQueryService.handle(query);
        if (driver.isEmpty()) return ResponseEntity.notFound().build();
        var driverResource = DriverResourceFromEntityAssembler.toResourceFromEntity(driver.get());
        return ResponseEntity.ok(driverResource);
    }
}