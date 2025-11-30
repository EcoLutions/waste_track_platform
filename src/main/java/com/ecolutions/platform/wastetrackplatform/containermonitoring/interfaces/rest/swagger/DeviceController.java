package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.CreateDeviceResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.DeviceResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "Device Management Endpoints")
public interface DeviceController {
    @PostMapping()
    @Operation(summary = "Create a new device", description = "Creates a new device in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DeviceResource> createDevice(@RequestBody CreateDeviceResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get device by ID", description = "Retrieves a device by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Device not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DeviceResource> getDeviceById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all devices", description = "Retrieves all devices.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<DeviceResource>> getAllDevices();
}
