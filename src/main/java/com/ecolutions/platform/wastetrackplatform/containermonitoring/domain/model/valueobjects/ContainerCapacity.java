package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ContainerCapacity(Integer volumeLiters, Integer maxWeightKg) {
    public ContainerCapacity {
        if (volumeLiters == null || volumeLiters <= 0) {
            throw new IllegalArgumentException("Volume liters cannot be null or less than or equal to zero");
        }
        if (maxWeightKg == null || maxWeightKg <= 0) {
            throw new IllegalArgumentException("Max weight kg cannot be null or less than or equal to zero");
        }
    }
}