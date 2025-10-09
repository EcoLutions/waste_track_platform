package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record RecipientId(String value) {
    public RecipientId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Recipient ID cannot be null or blank");
        }
    }

    public static RecipientId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new RecipientId(value);
    }

    public static String toStringOrNull(RecipientId recipientId) {
        return recipientId != null ? recipientId.value() : null;
    }
}