package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.CreateSensorReadingResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.UpdateSensorReadingResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.SensorReadingResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/sensor-readings", produces = APPLICATION_JSON_VALUE)
@Tag(name = "SensorReading", description = "Sensor Reading Management Endpoints")
public interface SensorReadingController {

    @PostMapping()
    @Operation(summary = "Create a new sensor reading", description = "Creates a new sensor reading in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensor reading created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<SensorReadingResource> createSensorReading(CreateSensorReadingResource resource);

    @PutMapping("/{id}")
    @Operation(summary = "Update sensor reading", description = "Updates an existing sensor reading by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor reading updated successfully."),
            @ApiResponse(responseCode = "404", description = "Sensor reading not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<SensorReadingResource> updateSensorReading(@PathVariable String id, UpdateSensorReadingResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sensor reading", description = "Deletes a sensor reading by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor reading deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Sensor reading not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteSensorReading(@PathVariable String id);

    @GetMapping("/{id}")
    @Operation(summary = "Get sensor reading by ID", description = "Retrieves a sensor reading by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor reading retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Sensor reading not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<SensorReadingResource> getSensorReadingById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all sensor readings", description = "Retrieves all sensor readings.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor readings retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<SensorReadingResource>> getAllSensorReadings();
}