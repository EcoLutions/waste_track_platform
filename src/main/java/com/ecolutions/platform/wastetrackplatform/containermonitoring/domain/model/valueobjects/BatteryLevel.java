package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record BatteryLevel(Integer percentage) {
    public BatteryLevel {
        if (percentage == null || percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Battery percentage must be between 0 and 100");
        }
    }

    public boolean isLow() {
        return percentage < 20;
    }

    public boolean requiresReplacement() {
        return percentage < 10;
    }

    public static Integer toIntegerOrNull(BatteryLevel batteryLevel) {
        return batteryLevel != null ? batteryLevel.percentage : null;
    }
}