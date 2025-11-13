package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects;

public enum WaypointStatus {
    PENDING,
    VISITED,
    SKIPPED;

    public static WaypointStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("WaypointStatus cannot be null or blank");
        }
        try {
            return WaypointStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid WaypointStatus: " + value);
        }
    }

    public static String toStringOrNull(WaypointStatus status) {
        return status != null ? status.name() : null;
    }
}