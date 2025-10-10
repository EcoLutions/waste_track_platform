package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.CreateRouteResource;
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
    ResponseEntity<List<RouteResource>> getAllRoutes();

    @GetMapping("/district/{districtId}/active")
    @Operation(summary = "Get active routes by district ID", description = "Retrieves all active routes (ASSIGNED or IN_PROGRESS status) for a specific district.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active routes retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<RouteResource>> getActiveRoutesByDistrictId(@PathVariable String districtId);
}