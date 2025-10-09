package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects;

public enum RouteType {
    REGULAR,
    EMERGENCY,
    OPTIMIZED;

    public static RouteType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RouteType cannot be null or blank");
        }
        try {
            return RouteType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid RouteType: " + value);
        }
    }
}