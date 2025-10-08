package com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Username(String value) {
    public Username {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("Username cannot exceed 50 characters");
        }
        if (!value.matches("^[a-zA-Z0-9._-]+$")) {
            throw new IllegalArgumentException("Username can only contain letters, numbers, dots, underscores, and hyphens");
        }
    }

    public static Username of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new Username(value);
    }

    public static String toStringOrNull(Username username) {
        return username != null ? username.value() : null;
    }
}