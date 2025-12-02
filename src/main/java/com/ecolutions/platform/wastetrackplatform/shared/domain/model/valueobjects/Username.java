package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Username(String value) {
    public Username {
        if (value == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        value = normalizeUsername(value);

        if (!isValid(value)) {
            throw new IllegalArgumentException(
                    "Username must be 3-50 characters, start with a letter, " +
                            "and contain only letters, numbers, spaces, dots (.), hyphens (-), and underscores (_)"
            );
        }
    }

    public static Username of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new Username(value);
    }

    public static String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            return username;
        }

        return username
                .trim()
                .replaceAll("\\s+", " ")
                .replaceAll("\\s+([._-])", "$1")
                .replaceAll("([._-])\\s+", "$1");
    }

    public static boolean isValid(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }

        if (username.length() < 3 || username.length() > 50) {
            return false;
        }

        return username.matches("^[A-Za-z][A-Za-z0-9 ._-]*[A-Za-z0-9._-]$") ||
                username.matches("^[A-Za-z]{3,50}$");  // Permite nombres cortos sin espacios
    }

    public static String toStringOrNull(Username username) {
        return username != null ? username.value() : null;
    }
}
