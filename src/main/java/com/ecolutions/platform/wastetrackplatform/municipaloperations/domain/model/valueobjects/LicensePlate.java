package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record LicensePlate(String value) {
    public LicensePlate {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("License plate cannot be null or blank");
        }
        // TODO: Add specific license plate format validation if needed
    }
}