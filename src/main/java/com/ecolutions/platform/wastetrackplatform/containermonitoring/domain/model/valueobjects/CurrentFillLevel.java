package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record CurrentFillLevel(Integer percentage) {
    public CurrentFillLevel {
        if (percentage == null || percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
    }

    public boolean isOverflowing() {
        return percentage > 90;
    }

    public boolean requiresCollection() {
        return percentage > 80;
    }

    public static Integer toIntegerOrNull(CurrentFillLevel currentFillLevel) {
        return currentFillLevel != null ? currentFillLevel.percentage : null;
    }
}