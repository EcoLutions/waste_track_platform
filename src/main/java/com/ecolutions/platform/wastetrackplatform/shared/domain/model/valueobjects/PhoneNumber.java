package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record PhoneNumber(String value) {
    public PhoneNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PhoneNumber cannot be null or empty");
        }
        if (!value.matches("^\\+519\\d{8}$")) {
            throw new IllegalArgumentException("Invalid Peruvian international mobile phone number format");
        }
    }

    public static PhoneNumber of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new PhoneNumber(value);
    }

    public static String toStringOrNull(PhoneNumber phoneNumber) {
        return phoneNumber != null ? phoneNumber.value() : null;
    }
}
