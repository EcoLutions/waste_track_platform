package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

public enum MessageType {
    ALERT,
    INFO,
    REMINDER,
    MARKETING;

    public static MessageType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("MessageType cannot be null or blank");
        }
        try {
            return MessageType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid MessageType: " + value);
        }
    }
}