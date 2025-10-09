package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record PaymentMethodId(String value) {
    public PaymentMethodId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PaymentMethodId cannot be null or empty");
        }
    }

    public static PaymentMethodId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new PaymentMethodId(value);
    }

    public static String toStringOrNull(PaymentMethodId paymentMethodId) {
        return paymentMethodId != null ? paymentMethodId.value() : null;
    }
}
