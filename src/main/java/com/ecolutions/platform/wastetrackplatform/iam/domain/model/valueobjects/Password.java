package com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Password(String value) {
    public Password {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("HashedPassword cannot be null or blank");
        }
        if (value.length() < 8) {
            throw new IllegalArgumentException("HashedPassword must be at least 8 characters long");
        }
    }

    public static Password of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new Password(value);
    }

    public static String toStringOrNull(Password hashedPassword) {
        return hashedPassword != null ? hashedPassword.value() : null;
    }
}