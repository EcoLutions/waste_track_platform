package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

/**
 * Command to mark a notification as read
 */
public record MarkNotificationAsReadCommand(String notificationId) {
    public MarkNotificationAsReadCommand {
        if (notificationId == null || notificationId.isBlank()) {
            throw new IllegalArgumentException("NotificationId cannot be null or blank");
        }
    }
}
