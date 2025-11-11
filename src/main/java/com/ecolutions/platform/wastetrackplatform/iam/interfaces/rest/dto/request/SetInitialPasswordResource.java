package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request;

public record SetInitialPasswordResource(String activationToken, String password) {
    public SetInitialPasswordResource {
        if (activationToken == null || activationToken.isBlank()) {
            throw new IllegalArgumentException("Activation token cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }
}
