package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

public enum DriverStatus {
    AVAILABLE,
    ON_ROUTE,
    OFF_DUTY,
    SUSPENDED;

    public static DriverStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DriverStatus cannot be null or blank");
        }
        try {
            return DriverStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid DriverStatus: " + value);
        }
    }
}