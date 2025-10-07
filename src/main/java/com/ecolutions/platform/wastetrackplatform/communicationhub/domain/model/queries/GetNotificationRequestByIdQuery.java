package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries;

public record GetNotificationRequestByIdQuery(String notificationRequestId) {
    public GetNotificationRequestByIdQuery {
        if (notificationRequestId == null || notificationRequestId.isBlank()) {
            throw new IllegalArgumentException("NotificationRequest ID cannot be null or blank");
        }
    }
}