package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record CreateUserResource(
    String username,
    String email,
    String password
) {
    public CreateUserResource {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }
}