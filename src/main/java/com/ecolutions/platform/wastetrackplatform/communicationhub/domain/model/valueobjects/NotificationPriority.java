package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

public enum NotificationPriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT;

    public static NotificationPriority fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("NotificationPriority cannot be null or blank");
        }
        try {
            return NotificationPriority.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid NotificationPriority: " + value);
        }
    }

    public static String toStringOrNull(NotificationPriority value) {
        return value == null ? null : value.toString();
    }
}