package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries;

public record GetUnreadNotificationsQuery(String userId) {
    public GetUnreadNotificationsQuery {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or blank");
        }
    }
}
