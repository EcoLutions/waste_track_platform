package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Mileage(Integer kilometers) {
    public Mileage {
        if (kilometers == null || kilometers < 0) {
            throw new IllegalArgumentException("Kilometers cannot be null or negative");
        }
    }

    public boolean needsMaintenance() {
        return kilometers > 10000;
    }

    public static Integer toIntegerOrNull(Mileage mileage) {
        return mileage == null ? null : mileage.kilometers;
    }
}