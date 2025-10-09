package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.CreateCitizenResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.UpdateCitizenResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response.CitizenResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/citizens", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Citizen", description = "Citizen Management Endpoints")
public interface CitizenController {

    @PostMapping()
    @Operation(summary = "Create a new citizen", description = "Creates a new citizen in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Citizen created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<CitizenResource> createCitizen(@RequestBody CreateCitizenResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get citizen by ID", description = "Retrieves a citizen by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citizen retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Citizen not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<CitizenResource> getCitizenById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all citizens", description = "Retrieves all citizens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citizens retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<CitizenResource>> getAllCitizens();

    @PutMapping("/{id}")
    @Operation(summary = "Update citizen", description = "Updates an existing citizen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citizen updated successfully."),
            @ApiResponse(responseCode = "404", description = "Citizen not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<CitizenResource> updateCitizen(@PathVariable String id, @RequestBody UpdateCitizenResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete citizen", description = "Deletes a citizen by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Citizen deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Citizen not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteCitizen(@PathVariable String id);
}