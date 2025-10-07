package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record DocumentNumber(String value) {
    public DocumentNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DocumentNumber cannot be null or empty");
        }
        if (!isValidPeruvianDocument(value)) {
            throw new IllegalArgumentException("Invalid Peruvian document number");
        }
    }

    private static boolean isValidPeruvianDocument(String value) {
        // DNI: 8 digits
        if (value.matches("\\d{8}")) {
            return true;
        }
        // RUC: 11 digits, starts with 10, 15, 17, or 20
        return value.matches("^(10|15|17|20)\\d{9}$");
    }

    public static DocumentNumber of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new DocumentNumber(value);
    }

    public static String toStringOrNull(DocumentNumber documentNumber) {
        return documentNumber != null ? documentNumber.value() : null;
    }
}
