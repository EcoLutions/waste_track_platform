package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.CreateReportResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.UpdateReportResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response.ReportResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/reports", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Report", description = "Report Management Endpoints")
public interface ReportController {

    @PostMapping()
    @Operation(summary = "Create a new report", description = "Creates a new report in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<ReportResource> createReport(@RequestBody CreateReportResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get report by ID", description = "Retrieves a report by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Report not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<ReportResource> getReportById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all reports", description = "Retrieves all reports.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<ReportResource>> getAllReports();

    @PutMapping("/{id}")
    @Operation(summary = "Update report", description = "Updates an existing report.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report updated successfully."),
            @ApiResponse(responseCode = "404", description = "Report not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<ReportResource> updateReport(@PathVariable String id, @RequestBody UpdateReportResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete report", description = "Deletes a report by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Report deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Report not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteReport(@PathVariable String id);
}