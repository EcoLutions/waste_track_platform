package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.CreatePlanCatalogResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.UpdatePlanCatalogResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.PlanCatalogResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/plan-catalogs", produces = APPLICATION_JSON_VALUE)
@Tag(name = "PlanCatalog", description = "Plan Catalog Management Endpoints")
public interface PlanCatalogController {

    @PostMapping()
    @Operation(summary = "Create a new plan catalog", description = "Creates a new plan catalog in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan catalog created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<PlanCatalogResource> createPlanCatalog(@RequestBody CreatePlanCatalogResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get plan catalog by ID", description = "Retrieves a plan catalog by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan catalog retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Plan catalog not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<PlanCatalogResource> getPlanCatalogById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all plan catalogs", description = "Retrieves all plan catalogs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan catalogs retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<PlanCatalogResource>> getAllPlanCatalogs();

    @PutMapping("/{id}")
    @Operation(summary = "Update plan catalog", description = "Updates an existing plan catalog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan catalog updated successfully."),
            @ApiResponse(responseCode = "404", description = "Plan catalog not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<PlanCatalogResource> updatePlanCatalog(@PathVariable String id, @RequestBody UpdatePlanCatalogResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete plan catalog", description = "Deletes a plan catalog by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plan catalog deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Plan catalog not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deletePlanCatalog(@PathVariable String id);
}