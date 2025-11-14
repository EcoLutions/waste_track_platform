package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

/**
 * Type of notification
 */
public enum NotificationType {
    // Route Planning notifications
    WAYPOINT_ADDED,
    WAYPOINT_REPLACED,
    WAYPOINT_REMOVED,
    CRITICAL_CONTAINER_NOT_ADDED,
    ROUTE_UPDATED,

    // System notifications
    INFO,
    WARNING,
    ERROR,
    SUCCESS;

    public static NotificationType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("NotificationType cannot be null or blank");
        }
        try {
            return NotificationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid NotificationType: " + value);
        }
    }

    public static String toStringOrNull(NotificationType value) {
        return value == null ? null : value.toString();
    }
}
