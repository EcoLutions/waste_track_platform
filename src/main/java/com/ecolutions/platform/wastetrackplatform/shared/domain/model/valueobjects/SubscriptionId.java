package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record SubscriptionId(String value) {
    public SubscriptionId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SubscriptionId cannot be null or empty");
        }
    }

    public static SubscriptionId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new SubscriptionId(value);
    }

    public static String toStringOrNull(SubscriptionId subscriptionId) {
        return subscriptionId != null ? subscriptionId.value() : null;
    }
}
