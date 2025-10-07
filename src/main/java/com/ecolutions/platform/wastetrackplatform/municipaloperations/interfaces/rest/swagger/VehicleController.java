package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.CreateVehicleResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateVehicleResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.VehicleResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/vehicles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Vehicle", description = "Vehicle Management Endpoints")
public interface VehicleController {

    @PostMapping()
    @Operation(summary = "Create a new vehicle", description = "Creates a new vehicle in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Vehicle created successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<VehicleResource> createVehicle(@RequestBody CreateVehicleResource resource);

    @PutMapping()
    @Operation(summary = "Update an existing vehicle", description = "Updates an existing vehicle in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehicle updated successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehicle not found."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<VehicleResource> updateVehicle(@RequestBody UpdateVehicleResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle by ID", description = "Deletes a vehicle by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Vehicle deleted successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehicle not found."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteVehicle(@PathVariable String id);

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID", description = "Retrieves a vehicle by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehicle retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehicle not found."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<VehicleResource> getVehicleById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all vehicles", description = "Retrieves all vehicles.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<VehicleResource>> getAllVehicles();
}