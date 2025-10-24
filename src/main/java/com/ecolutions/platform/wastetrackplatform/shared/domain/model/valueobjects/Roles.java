package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

public enum Roles {
    ROLE_CITIZEN,
    ROLE_DRIVER,
    ROLE_MUNICIPAL_ADMINISTRATOR,
    ROLE_SYSTEM_ADMINISTRATOR;

    public static Roles fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Roles cannot be null or blank");
        }
        try {
            return Roles.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Roles: " + value);
        }
    }

    public String toStringOrNull(Roles role) {
        return role != null ? role.name() : null;
    }
}
