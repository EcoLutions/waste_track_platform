package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

public record PaymentId(String paymentId) {
    public PaymentId {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("Payment ID cannot be null or blank");
        }
    }

    public static PaymentId of(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            return null;
        }
        return new PaymentId(paymentId);
    }

    public static String toStringOrNull(PaymentId paymentId) {
        return paymentId != null ? paymentId.paymentId() : null;
    }
}