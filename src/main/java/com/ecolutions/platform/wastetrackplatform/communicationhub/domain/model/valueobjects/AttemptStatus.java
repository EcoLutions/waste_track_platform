package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

public enum AttemptStatus {
    PENDING,
    DELIVERED,
    FAILED,
    BOUNCED;

    public static AttemptStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AttemptStatus cannot be null or blank");
        }
        try {
            return AttemptStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid AttemptStatus: " + value);
        }
    }
}