package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.CreateDeliveryAttemptResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.UpdateDeliveryAttemptResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.DeliveryAttemptResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "DeliveryAttempt", description = "DeliveryAttempt Management Endpoints")
public interface DeliveryAttemptController {

    @Operation(summary = "Create a new delivery attempt", description = "Creates a new delivery attempt in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "DeliveryAttempt created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DeliveryAttemptResource> createDeliveryAttempt(CreateDeliveryAttemptResource resource);

    @Operation(summary = "Get delivery attempt by ID", description = "Retrieves a delivery attempt by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DeliveryAttempt retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "DeliveryAttempt not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DeliveryAttemptResource> getDeliveryAttemptById(String id);

    @Operation(summary = "Get all delivery attempts", description = "Retrieves all delivery attempts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DeliveryAttempts retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<DeliveryAttemptResource>> getAllDeliveryAttempts();

    @Operation(summary = "Update delivery attempt", description = "Updates an existing delivery attempt.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DeliveryAttempt updated successfully."),
            @ApiResponse(responseCode = "404", description = "DeliveryAttempt not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<DeliveryAttemptResource> updateDeliveryAttempt(UpdateDeliveryAttemptResource resource);

    @Operation(summary = "Delete delivery attempt", description = "Deletes a delivery attempt by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "DeliveryAttempt deleted successfully."),
            @ApiResponse(responseCode = "404", description = "DeliveryAttempt not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteDeliveryAttempt(String id);
}