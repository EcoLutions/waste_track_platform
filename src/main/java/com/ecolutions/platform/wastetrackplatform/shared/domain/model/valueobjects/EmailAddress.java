package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record EmailAddress(String value) {
    public EmailAddress {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("EmailAddress cannot be null or empty");
        }
        if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("EmailAddress is not valid");
        }
    }

    public static EmailAddress of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new EmailAddress(value);
    }

    public static String toStringOrNull(EmailAddress emailAddress) {
        return emailAddress != null ? emailAddress.value() : null;
    }
}
