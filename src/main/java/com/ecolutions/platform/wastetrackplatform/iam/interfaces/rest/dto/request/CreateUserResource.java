package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request;

import java.util.List;

public record CreateUserResource(String email, List<String> roles) {
    public CreateUserResource {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or blank");
        }
    }
}
