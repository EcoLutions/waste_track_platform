package com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects;


import jakarta.validation.constraints.NotNull;

public enum UserType {
    CITIZEN,
    DRIVER,
    ADMINISTRATOR,
    SUPER_ADMINISTRATOR;

    public static UserType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UserType cannot be null or blank");
        }
        try {
            return UserType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UserType: " + value);
        }
    }

    public static String toStringOrNull(@NotNull UserType userType) {
        return userType != null ? userType.name() : null;
    }
}