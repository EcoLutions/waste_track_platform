package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects;

public enum PriorityLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public static PriorityLevel fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PriorityLevel cannot be null or blank");
        }
        try {
            return PriorityLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid PriorityLevel: " + value);
        }
    }
}