package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries;

public record GetCitizenByUserIdQuery(String userId) {
    public GetCitizenByUserIdQuery {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
    }
}
