package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

public enum ValidationStatus {
    VALID,
    ANOMALY,
    SENSOR_ERROR;

    public static ValidationStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ValidationStatus cannot be null or blank");
        }
        try {
            return ValidationStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ValidationStatus: " + value);
        }
    }
}