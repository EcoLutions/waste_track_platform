package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record EmailAddress(String value) {
    public EmailAddress {
        String trimmedValue = value.trim().toLowerCase();

        if (!isValidEmail(trimmedValue)) {
            throw new IllegalArgumentException("Invalid email address: " + value);
        }

        value = trimmedValue;
    }

    public static EmailAddress of(String value) {
        String trimmedValue = value.trim().toLowerCase();

        if (!isValidEmail(trimmedValue)) {
            return null;
        }

        return new EmailAddress(trimmedValue);
    }

    public static String toStringOrNull(EmailAddress emailAddress) {
        return emailAddress != null ? emailAddress.value() : null;
    }

    private static boolean isValidEmail(String email) {
        return email != null &&
                email.matches("^[A-Za-z0-9+_.-]+@(.+)$") &&
                email.length() <= 254;
    }
}
