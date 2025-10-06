package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record FullName(
    String firstName,
    String lastName
) {
    public FullName {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
    }
}
