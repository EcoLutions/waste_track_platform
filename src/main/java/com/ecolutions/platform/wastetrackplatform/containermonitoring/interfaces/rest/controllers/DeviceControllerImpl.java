package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllDevicesQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetDeviceByIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.DeviceCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries.DeviceQueryService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.CreateDeviceResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.DeviceResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromentitytoresponse.DeviceResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromresourcetocommand.CreateDeviceCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.swagger.DeviceController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DeviceControllerImpl implements DeviceController {
    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    @Override
    public ResponseEntity<DeviceResource> createDevice(CreateDeviceResource resource) {
        var command = CreateDeviceCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdDevice = deviceCommandService.handle(command);
        if (createdDevice.isEmpty()) return ResponseEntity.badRequest().build();
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(createdDevice.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(deviceResource.id())
                .toUri();
        return ResponseEntity.created(location).body(deviceResource);
    }

    @Override
    public ResponseEntity<DeviceResource> getDeviceById(String id) {
        var query = new GetDeviceByIdQuery(id);
        var device = deviceQueryService.handle(query);
        if (device.isEmpty()) return ResponseEntity.notFound().build();
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(device.get());
        return ResponseEntity.ok(deviceResource);
    }

    @Override
    public ResponseEntity<List<DeviceResource>> getAllDevices() {
        var query = new GetAllDevicesQuery();
        var devices = deviceQueryService.handle(query);
        var deviceResources = devices.stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(deviceResources);
    }
}
