package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record SensorId(String sensorId) {
    public SensorId {
        if (sensorId == null || sensorId.isBlank()) {
            throw new IllegalArgumentException("Sensor ID cannot be null or blank");
        }
    }

    public static SensorId of(String sensorId) {
        if (sensorId == null || sensorId.isBlank()) {
            return null;
        }
        return new SensorId(sensorId);
    }

    public static String toStringOrNull(SensorId sensorId) {
        return sensorId != null ? sensorId.sensorId() : null;
    }
}