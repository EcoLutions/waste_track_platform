package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.CreateContainerResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.UpdateContainerResource;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.ContainerResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/containers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Container", description = "Container Management Endpoints")
public interface ContainerController {
    @PostMapping()
    @Operation(summary = "Create a new container", description = "Creates a new container in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Container created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<ContainerResource> createContainer(@RequestBody CreateContainerResource resource);

    @PutMapping("/{id}")
    @Operation(summary = "Update container", description = "Updates an existing container in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Container updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "Container not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<ContainerResource> updateContainer(@PathVariable String id, @RequestBody UpdateContainerResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete container", description = "Deletes a container from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Container deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Container not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteContainer(@PathVariable String id);

    @GetMapping("/{id}")
    @Operation(summary = "Get container by ID", description = "Retrieves a container by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Container retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Container not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<ContainerResource> getContainerById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all containers", description = "Retrieves all containers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Containers retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<ContainerResource>> getAllContainers();

    @GetMapping("/district/{districtId}")
    @Operation(summary = "Get containers by district ID", description = "Retrieves all containers for a specific district.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Containers retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid district ID."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<ContainerResource>> getContainersByDistrictId(@PathVariable String districtId);
}