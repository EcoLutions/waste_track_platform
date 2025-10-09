package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects;
public enum PaymentMethodType {
    CREDIT_CARD,
    BANK_TRANSFER;

    public static PaymentMethodType fromString(String type) {
        try {
            return PaymentMethodType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment method type: " + type, e);
        }
    }

    public static String toStringOrNull(PaymentMethodType type) {
        return type != null ? type.name() : null;
    }
}