package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.CreateNotificationRequestResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.UpdateNotificationRequestResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.NotificationRequestResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/notification-requests", produces = APPLICATION_JSON_VALUE)
@Tag(name = "NotificationRequest", description = "NotificationRequest Management Endpoints")
public interface NotificationRequestController {

    @PostMapping()
    @Operation(summary = "Create a new notification request", description = "Creates a new notification request in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "NotificationRequest created successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<NotificationRequestResource> createNotificationRequest(@RequestBody CreateNotificationRequestResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get notification request by ID", description = "Retrieves a notification request by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "NotificationRequest retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NotificationRequest not found."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<NotificationRequestResource> getNotificationRequestById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all notification requests", description = "Retrieves all notification requests.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "NotificationRequests retrieved successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<NotificationRequestResource>> getAllNotificationRequests();

    @PutMapping("/{id}")
    @Operation(summary = "Update notification request", description = "Updates an existing notification request.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "NotificationRequest updated successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NotificationRequest not found."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<NotificationRequestResource> updateNotificationRequest(@PathVariable String id, @RequestBody UpdateNotificationRequestResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification request", description = "Deletes a notification request by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "NotificationRequest deleted successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NotificationRequest not found."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteNotificationRequest(@PathVariable String id);
}