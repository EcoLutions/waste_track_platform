package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.CreatePaymentMethodResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.UpdatePaymentMethodResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.PaymentMethodResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/payment-methods", produces = APPLICATION_JSON_VALUE)
@Tag(name = "PaymentMethod", description = "Payment Method Management Endpoints")
public interface PaymentMethodController {

    @PostMapping()
    @Operation(summary = "Create a new payment method", description = "Creates a new payment method in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment method created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<PaymentMethodResource> createPaymentMethod(@RequestBody CreatePaymentMethodResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get payment method by ID", description = "Retrieves a payment method by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Payment method not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<PaymentMethodResource> getPaymentMethodById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all payment methods", description = "Retrieves all payment methods.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment methods retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<PaymentMethodResource>> getAllPaymentMethods();

    @PutMapping("/{id}")
    @Operation(summary = "Update payment method", description = "Updates an existing payment method.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method updated successfully."),
            @ApiResponse(responseCode = "404", description = "Payment method not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<PaymentMethodResource> updatePaymentMethod(@PathVariable String id, @RequestBody UpdatePaymentMethodResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment method", description = "Deletes a payment method by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment method deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Payment method not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deletePaymentMethod(@PathVariable String id);
}