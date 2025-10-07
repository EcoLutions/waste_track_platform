package com.ecolutions.platform.wastetrackplatform.profile.domain.model.queries;

public record GetUserProfileByIdQuery(String userProfileId) {
    public GetUserProfileByIdQuery {
        if (userProfileId == null || userProfileId.isBlank()) {
            throw new IllegalArgumentException("User Profile ID cannot be null or blank");
        }
    }
}