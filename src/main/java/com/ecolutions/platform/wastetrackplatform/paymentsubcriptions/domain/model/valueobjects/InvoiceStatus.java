package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects;

public enum InvoiceStatus {
    DRAFT,
    ISSUED,
    PAID,
    OVERDUE,
    VOID;

    public static InvoiceStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("InvoiceStatus cannot be null or blank");
        }
        try {
            return InvoiceStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid InvoiceStatus: " + value);
        }
    }

    public static String toStringOrNull(InvoiceStatus status) {
        return status != null ? status.name() : null;
    }
}