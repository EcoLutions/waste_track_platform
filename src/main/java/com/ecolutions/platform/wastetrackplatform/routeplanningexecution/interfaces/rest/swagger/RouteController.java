package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.CreateRouteResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateCurrentLocationResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateRouteResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response.RouteResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/routes", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Route", description = "Route Management Endpoints")
public interface RouteController {

    @PostMapping()
    @Operation(summary = "Create a new route", description = "Creates a new route in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Route created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<RouteResource> createRoute(@RequestBody CreateRouteResource resource);

    @PutMapping("/{id}")
    @Operation(summary = "Update route by ID", description = "Updates an existing route by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route updated successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<RouteResource> updateRoute(@PathVariable String id, @RequestBody UpdateRouteResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete route by ID", description = "Deletes a route by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Route deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteRoute(@PathVariable String id);

    @GetMapping("/{id}")
    @Operation(summary = "Get route by ID", description = "Retrieves a route by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<RouteResource> getRouteById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all routes", description = "Retrieves all routes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<RouteResource>> getAllRoutes(
           @RequestParam(required = false) String districtId,
           @RequestParam(required = false) String driverId,
           @RequestParam(required = false) String vehicleId,
           @RequestParam(required = false) String status,
           @RequestParam(required = false) List<String> statuses
    );

    @GetMapping("/district/{districtId}/active")
    @Operation(summary = "Get active routes by district ID", description = "Retrieves all active routes (ASSIGNED or IN_PROGRESS status) for a specific district.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active routes retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<RouteResource>> getActiveRoutesByDistrictId(@PathVariable String districtId);

    @PostMapping("/{id}/generate-waypoints")
    @Operation(summary = "Generate optimized waypoints for route", description = "Generates optimized waypoints for a route using Google Maps API. " + "Selects containers based on fill level priority (>=90% CRITICAL, >=80% HIGH, >=70% MEDIUM, <70% LOW), " + "optimizes route order, and adjusts to fit within district's maxRouteDuration. " + "Route must be in ASSIGNED status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Waypoints generated successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "400", description = "Route cannot be modified (not in ASSIGNED status) or no containers available."),
            @ApiResponse(responseCode = "500", description = "Internal server error or Google Maps API error.")
    })
    ResponseEntity<RouteResource> generateOptimizedWaypoints(@PathVariable String id);

    @PatchMapping("/{id}/current-location")
    @Operation(summary = "Update route current location", description = "Updates the current GPS location of an in-progress route. " + "This endpoint should be called periodically by the driver's mobile app to track route progress. " + "The updated location is broadcasted via WebSocket to /topic/routes/{routeId}/location for real-time tracking. " + "Route must be in IN_PROGRESS status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current location updated successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "400", description = "Route is not IN_PROGRESS or invalid coordinates."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<RouteResource> updateCurrentLocation(@PathVariable String id, @RequestBody UpdateCurrentLocationResource resource);

    @PostMapping("/{id}/start")
    @Operation(summary = "Start route execution", description = "Starts the execution of a route. Route must be in ACTIVE status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route started successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "400", description = "Route cannot be started (not in ACTIVE status)."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<RouteResource> startRoute(@PathVariable String id);

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete route execution", description = "Completes the execution of a route. Route must be in IN_PROGRESS status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route completed successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "400", description = "Route cannot be completed (not in IN_PROGRESS status)."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<RouteResource> completeRoute(@PathVariable String id);

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel route", description = "Cancels a route. Route cannot be already COMPLETED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route cancelled successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "400", description = "Route cannot be cancelled (already COMPLETED)."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<RouteResource> cancelRoute(@PathVariable String id);

    @PostMapping("/{id}/re-optimize")
    @Operation(summary = "Re-optimize route", description = "Re-optimizes the route waypoints order based on current conditions. Route must be in ACTIVE status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route re-optimized successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "400", description = "Route cannot be re-optimized (not in ACTIVE status)."),
            @ApiResponse(responseCode = "500", description = "Internal server error or Google Maps API error.")
    })
    ResponseEntity<RouteResource> reOptimizeRoute(@PathVariable String id);

    @PostMapping("/{id}/update-estimates")
    @Operation(summary = "Update route estimates", description = "Updates the distance and duration estimates for the route. Route must be in ACTIVE status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route estimates updated successfully."),
            @ApiResponse(responseCode = "404", description = "Route not found."),
            @ApiResponse(responseCode = "400", description = "Route cannot be updated (not in ACTIVE status)."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<RouteResource> updateRouteEstimates(@PathVariable String id);

    @PostMapping("/{id}/waypoints/{waypointId}/mark-visited")
    @Operation(summary = "Mark waypoint as visited", description = "Marks a waypoint within the route as visited with the actual arrival time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Waypoint marked as visited successfully."),
            @ApiResponse(responseCode = "404", description = "Waypoint not found."),
            @ApiResponse(responseCode = "400", description = "Waypoint cannot be marked as visited."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<RouteResource> markWaypointAsVisited(@PathVariable String id, @PathVariable String waypointId);
}