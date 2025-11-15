package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.CreateDistrictResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateDistrictResource;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.DistrictResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/districts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "District", description = "District Management Endpoints")
public interface DistrictController {

    @PostMapping()
    @Operation(summary = "Create a new district", description = "Creates a new district in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "District created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DistrictResource> createDistrict(@RequestBody CreateDistrictResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get district by ID", description = "Retrieves a district by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "District retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "District not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DistrictResource> getDistrictById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all districts", description = "Retrieves all districts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Districts retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<DistrictResource>> getAllDistricts();

    @PutMapping("/{id}")
    @Operation(summary = "Update district", description = "Updates an existing district.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "District updated successfully."),
            @ApiResponse(responseCode = "404", description = "District not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DistrictResource> updateDistrict(@PathVariable String id, @RequestBody UpdateDistrictResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete district", description = "Deletes a district by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "District deleted successfully."),
            @ApiResponse(responseCode = "404", description = "District not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteDistrict(@PathVariable String id);
}