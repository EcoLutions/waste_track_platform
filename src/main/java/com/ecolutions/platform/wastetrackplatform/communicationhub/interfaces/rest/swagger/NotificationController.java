package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.NotificationResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Swagger interface for Notification controller
 */
@Tag(name = "Notifications", description = "Notification management endpoints")
@RequestMapping("/api/v1/notifications")
public interface NotificationController {

    @Operation(
            summary = "Get all notifications for a user",
            description = "Retrieve all notifications for a specific user, ordered by creation date (newest first)"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved notifications")
    @GetMapping("/user/{userId}")
    ResponseEntity<List<NotificationResource>> getNotificationsByUserId(@PathVariable String userId);

    @Operation(
            summary = "Get unread notifications for a user",
            description = "Retrieve only unread notifications for a specific user"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved unread notifications")
    @GetMapping("/user/{userId}/unread")
    ResponseEntity<List<NotificationResource>> getUnreadNotifications(@PathVariable String userId);

    @Operation(
            summary = "Mark notification as read",
            description = "Mark a specific notification as read"
    )
    @ApiResponse(responseCode = "200", description = "Notification marked as read successfully")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    @PatchMapping("/{notificationId}/read")
    ResponseEntity<NotificationResource> markAsRead(@PathVariable String notificationId);
}
