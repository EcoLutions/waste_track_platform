package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserResource(
        String id,
        String email,
        String status,
        Integer failedLoginAttempts,
        String lastLoginAt,
        String passwordChangedAt,
        String createdAt,
        String updatedAt,
        List<String> roles
) {
}
