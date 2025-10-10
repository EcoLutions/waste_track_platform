package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.CreateDriverResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateDriverResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.DriverResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/drivers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Driver", description = "Driver Management Endpoints")
public interface DriverController {

    @PostMapping()
    @Operation(summary = "Create a new driver", description = "Creates a new driver in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Driver created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DriverResource> createDriver(@RequestBody CreateDriverResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get driver by ID", description = "Retrieves a driver by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Driver not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DriverResource> getDriverById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all drivers", description = "Retrieves all drivers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Drivers retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<DriverResource>> getAllDrivers();

    @GetMapping("/district/{districtId}")
    @Operation(summary = "Get all drivers by district ID", description = "Retrieves all drivers by district ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Drivers retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<DriverResource>> getAllDriversByDistrictId(@PathVariable String districtId);

    @PutMapping()
    @Operation(summary = "Update driver", description = "Updates an existing driver.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver updated successfully."),
            @ApiResponse(responseCode = "404", description = "Driver not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DriverResource> updateDriver(@RequestBody UpdateDriverResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete driver", description = "Deletes a driver by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Driver deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Driver not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteDriver(@PathVariable String id);
}