package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

public enum ProviderType {
    SENDGRID,
    TWILIO,
    FIREBASE;

    public static ProviderType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProviderType cannot be null or blank");
        }
        try {
            return ProviderType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ProviderType: " + value);
        }
    }
}