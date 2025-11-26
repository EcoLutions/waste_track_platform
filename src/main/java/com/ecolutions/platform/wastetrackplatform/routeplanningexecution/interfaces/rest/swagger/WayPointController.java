package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.CreateWayPointResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.MarkWayPointAsVisitedResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateWayPointResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response.WayPointResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/waypoints", produces = APPLICATION_JSON_VALUE)
@Tag(name = "WayPoint", description = "WayPoint Management Endpoints")
public interface WayPointController {

    @PostMapping()
    @Operation(summary = "Create a new waypoint", description = "Creates a new waypoint in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "WayPoint created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<WayPointResource> createWayPoint(@RequestBody CreateWayPointResource resource, @RequestParam String routeId);

    @GetMapping("/{id}")
    @Operation(summary = "Get waypoint by ID", description = "Retrieves a waypoint by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WayPoint retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "WayPoint not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<WayPointResource> getWayPointById(@Parameter(description = "WayPoint ID") @PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all waypoints", description = "Retrieves all waypoints.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WayPoints retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<WayPointResource>> getAllWayPoints(@RequestParam(required = false) String routeId);

    @PutMapping("/{id}")
    @Operation(summary = "Update waypoint", description = "Updates an existing waypoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WayPoint updated successfully."),
            @ApiResponse(responseCode = "404", description = "WayPoint not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<WayPointResource> updateWayPoint(@Parameter(description = "WayPoint ID") @PathVariable String id, @RequestBody UpdateWayPointResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete waypoint", description = "Deletes an existing waypoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "WayPoint deleted successfully."),
            @ApiResponse(responseCode = "404", description = "WayPoint not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteWayPoint(@Parameter(description = "WayPoint ID") @PathVariable String id);

    @PatchMapping("/{id}/mark-visited")
    @Operation(summary = "Mark waypoint as visited", description = "Marks a waypoint as visited with the actual arrival time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WayPoint marked as visited successfully."),
            @ApiResponse(responseCode = "404", description = "WayPoint not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data or waypoint cannot be visited."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<WayPointResource> markWayPointAsVisited(@Parameter(description = "WayPoint ID") @PathVariable String id, @RequestBody MarkWayPointAsVisitedResource resource);
}