package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries;

public record GetNotificationsByUserIdQuery(String userId) {
    public GetNotificationsByUserIdQuery {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or blank");
        }
    }
}
