package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

public record DeleteNotificationRequestCommand(String notificationRequestId) {
    public DeleteNotificationRequestCommand {
        if (notificationRequestId == null || notificationRequestId.isBlank()) {
            throw new IllegalArgumentException("NotificationRequest ID cannot be null or blank");
        }
    }
}