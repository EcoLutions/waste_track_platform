package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

public enum NotificationChannel {
    EMAIL,
    SMS,
    PUSH;

    public static NotificationChannel fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("NotificationChannel cannot be null or blank");
        }
        try {
            return NotificationChannel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid NotificationChannel: " + value);
        }
    }
}