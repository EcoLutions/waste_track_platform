package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response.EvidenceResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequestMapping(value = "/api/v1/evidences", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Evidence", description = "Evidence Management Endpoints")
public interface EvidenceController {

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a new evidence", description = "Uploads a new evidence file to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evidence uploaded successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<EvidenceResource> uploadEvidence(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file);

    @GetMapping("/{id}")
    @Operation(summary = "Get evidence by ID", description = "Retrieves an evidence by its ID with a fresh download URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidence retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Evidence not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<EvidenceResource> getEvidenceById(@PathVariable String id) throws IOException;

    @GetMapping()
    @Operation(summary = "Get all evidences", description = "Retrieves all evidences.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidences retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<EvidenceResource>> getAllEvidences();
}