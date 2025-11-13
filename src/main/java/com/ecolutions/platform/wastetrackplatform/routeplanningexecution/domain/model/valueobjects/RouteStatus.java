package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects;

public enum RouteStatus {
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public static RouteStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RouteStatus cannot be null or blank");
        }
        try {
            return RouteStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid RouteStatus: " + value);
        }
    }
}