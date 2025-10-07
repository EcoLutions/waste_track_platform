package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

public enum RecipientType {
    CITIZEN,
    DRIVER,
    ADMINISTRATOR;

    public static RecipientType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RecipientType cannot be null or blank");
        }
        try {
            return RecipientType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid RecipientType: " + value);
        }
    }
}