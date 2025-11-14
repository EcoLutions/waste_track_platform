package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

/**
 * Status of a notification
 */
public enum NotificationStatus {
    UNREAD,
    READ;

    public static NotificationStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("NotificationStatus cannot be null or blank");
        }
        try {
            return NotificationStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid NotificationStatus: " + value);
        }
    }

    public static String toStringOrNull(NotificationStatus value) {
        return value == null ? null : value.toString();
    }
}
