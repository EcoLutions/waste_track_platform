package com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects;

public enum RoleType {
    SYSTEM,
    CUSTOM;

    public static RoleType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RoleType cannot be null or blank");
        }
        try {
            return RoleType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid RoleType: " + value);
        }
    }

    public static String toStringOrNull(RoleType roleType) {
        return roleType != null ? roleType.name() : null;
    }
}