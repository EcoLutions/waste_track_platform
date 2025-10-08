package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record UpdateUserResource(
    String username,
    String email,
    String accountStatus
) {
    public UpdateUserResource {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (accountStatus == null || accountStatus.isBlank()) {
            throw new IllegalArgumentException("Account status cannot be null or blank");
        }
    }
}