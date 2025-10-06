package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

public enum OperationalStatus {
    ACTIVE,
    SUSPENDED,
    TRIAL;

    public static OperationalStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("OperationalStatus cannot be null or blank");
        }
        try {
            return OperationalStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid OperationalStatus: " + value);
        }
    }
}