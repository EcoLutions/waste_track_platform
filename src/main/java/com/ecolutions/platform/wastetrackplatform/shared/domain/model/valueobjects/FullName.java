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

    public static String firstNameOrNull(FullName fullName) {
        return fullName != null ? fullName.firstName : null;
    }

    public static String lastNameOrNull(FullName fullName) {
        return fullName != null ? fullName.lastName : null;
    }
}
