package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.SubscriptionResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/api/v1/subscriptions", produces = "application/json")
@Tag(name = "Subscription", description = "Subscription Management Endpoints")
public interface SubscriptionController {
    @GetMapping("/{id}")
    @Operation(summary = "Get subscription by ID", description = "Retrieves a subscription by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Subscription not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<SubscriptionResource> getSubscriptionById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all subscriptions", description = "Retrieves all subscriptions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<SubscriptionResource>> getAllSubscriptions();
}