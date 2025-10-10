package com.ecolutions.platform.wastetrackplatform.profile.domain.model.queries;

public record GetUserProfileByUserIdQuery(String userId) {
    public GetUserProfileByUserIdQuery {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
    }
}
