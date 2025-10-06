package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record DocumentNumber(String value) {
    public DocumentNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DocumentNumber cannot be null or empty");
        }
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
