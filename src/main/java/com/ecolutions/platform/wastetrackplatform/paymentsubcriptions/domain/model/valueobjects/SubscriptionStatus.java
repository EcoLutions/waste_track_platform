package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects;

public enum SubscriptionStatus {
    TRIAL,
    ACTIVE,
    SUSPENDED,
    CANCELLED;

    public static SubscriptionStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SubscriptionStatus cannot be null or blank");
        }
        try {
            return SubscriptionStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid SubscriptionStatus: " + value);
        }
    }

    public static String toStringOrNull(SubscriptionStatus status) {
        return status != null ? status.name() : null;
    }
}