package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record UserResource(
    String id,
    String username,
    String email,
    String accountStatus,
    Integer failedLoginAttempts,
    String lastLoginAt,
    String passwordChangedAt,
    String createdAt,
    String updatedAt
) {
}