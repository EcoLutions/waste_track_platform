package com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("HashedPassword cannot be null or blank");
        }
        if (value.length() < 8) {
            throw new IllegalArgumentException("HashedPassword must be at least 8 characters long");
        }
    }

    public static HashedPassword of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new HashedPassword(value);
    }

    public static String toStringOrNull(HashedPassword hashedPassword) {
        return hashedPassword != null ? hashedPassword.value() : null;
    }
}