package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record WeightCapacity(Integer kilograms) {
    public WeightCapacity {
        if (kilograms == null || kilograms <= 0) {
            throw new IllegalArgumentException("Kilograms must be greater than zero");
        }
    }
}