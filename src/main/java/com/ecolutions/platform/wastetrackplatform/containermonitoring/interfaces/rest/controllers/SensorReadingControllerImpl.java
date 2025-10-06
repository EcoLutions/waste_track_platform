package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllSensorReadingsQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetSensorReadingByIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.SensorReadingCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries.SensorReadingQueryService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.CreateSensorReadingResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.SensorReadingResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromentitytoresponse.SensorReadingResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromresourcetocommand.CreateSensorReadingCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.swagger.SensorReadingController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SensorReadingControllerImpl implements SensorReadingController {
    private final SensorReadingCommandService sensorReadingCommandService;
    private final SensorReadingQueryService sensorReadingQueryService;

    @Override
    public ResponseEntity<SensorReadingResource> createSensorReading(CreateSensorReadingResource resource) {
        var command = CreateSensorReadingCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdSensorReading = sensorReadingCommandService.handle(command);
        if (createdSensorReading.isEmpty()) return ResponseEntity.badRequest().build();
        var sensorReadingResource = SensorReadingResourceFromEntityAssembler.toResourceFromEntity(createdSensorReading.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sensorReadingResource.id())
                .toUri();
        return ResponseEntity.created(location).body(sensorReadingResource);
    }

    @Override
    public ResponseEntity<SensorReadingResource> getSensorReadingById(String id) {
        var query = new GetSensorReadingByIdQuery(id);
        var sensorReading = sensorReadingQueryService.handle(query);
        if (sensorReading.isEmpty()) return ResponseEntity.notFound().build();
        var sensorReadingResource = SensorReadingResourceFromEntityAssembler.toResourceFromEntity(sensorReading.get());
        return ResponseEntity.ok(sensorReadingResource);
    }

    @Override
    public ResponseEntity<List<SensorReadingResource>> getAllSensorReadings() {
        var query = new GetAllSensorReadingsQuery();
        var sensorReadings = sensorReadingQueryService.handle(query);
        var sensorReadingResources = sensorReadings.stream()
                .map(SensorReadingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(sensorReadingResources);
    }
}