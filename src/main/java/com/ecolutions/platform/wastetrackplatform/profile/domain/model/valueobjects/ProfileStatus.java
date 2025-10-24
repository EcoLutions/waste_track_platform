package com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects;

public enum ProfileStatus {
    INCOMPLETE,
    COMPLETE;

    public static ProfileStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProfileStatus cannot be null or blank");
        }
        try {
            return ProfileStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ProfileStatus: " + value);
        }
    }

    public String toStringOrNull(ProfileStatus status) {
        return status != null ? status.name() : null;
    }
}
