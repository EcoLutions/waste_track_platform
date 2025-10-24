package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects;

public enum BillingPeriod {
    MONTHLY,
    QUARTERLY,
    SEMI_ANNUAL,
    ANNUAL;

    public static BillingPeriod fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("BillingPeriod cannot be null or blank");
        }
        try {
            return BillingPeriod.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String toStringOrNull(BillingPeriod period) {
        return period != null ? period.name() : null;
    }
}
