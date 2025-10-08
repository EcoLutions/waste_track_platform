package com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects;

public enum AccountStatus {
    ACTIVE,
    LOCKED,
    PENDING_ACTIVATION,
    DISABLED;

    public static AccountStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AccountStatus cannot be null or blank");
        }
        try {
            return AccountStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid AccountStatus: " + value);
        }
    }

    public static String toStringOrNull(AccountStatus status) {
        return status != null ? status.name() : null;
    }
}