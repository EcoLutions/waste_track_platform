package com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries;

public record GetUserByIdQuery(String userId) {
    public GetUserByIdQuery {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
    }
}