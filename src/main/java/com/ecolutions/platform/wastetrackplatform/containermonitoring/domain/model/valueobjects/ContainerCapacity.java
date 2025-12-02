package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ContainerCapacity(Integer volumeLiters, Integer maxFillLevel) {
    public ContainerCapacity {
        if (volumeLiters == null || volumeLiters <= 0) {
            throw new IllegalArgumentException("Volume liters cannot be null or less than or equal to zero");
        }
        if (maxFillLevel == null || maxFillLevel <= 0 || maxFillLevel >= 100) {
            throw new IllegalArgumentException("Max fill level cannot be null or less than or equal to zero nor greater than or equal to 100");
        }
    }

    public static Integer volumeLitersToIntegerOrNull(ContainerCapacity containerCapacity) {
        return containerCapacity == null ? null : containerCapacity.volumeLiters();
    }

    public static Integer maxFillLevelToIntegerOrNull(ContainerCapacity containerCapacity) {
        return containerCapacity == null ? null : containerCapacity.maxFillLevel();
    }
}