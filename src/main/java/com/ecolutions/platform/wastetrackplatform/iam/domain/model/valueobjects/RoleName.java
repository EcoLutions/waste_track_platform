package com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record RoleName(String value) {
    public RoleName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RoleName cannot be null or blank");
        }
        if (value.length() < 2) {
            throw new IllegalArgumentException("RoleName must be at least 2 characters long");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("RoleName cannot exceed 100 characters");
        }
        if (!value.matches("^[a-zA-Z0-9._\\s-]+$")) {
            throw new IllegalArgumentException("RoleName can only contain letters, numbers, dots, underscores, spaces, and hyphens");
        }
    }

    public static RoleName of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new RoleName(value);
    }

    public static String toStringOrNull(RoleName roleName) {
        return roleName != null ? roleName.value() : null;
    }
}