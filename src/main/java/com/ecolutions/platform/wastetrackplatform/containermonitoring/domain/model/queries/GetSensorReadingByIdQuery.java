package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries;

public record GetSensorReadingByIdQuery(String sensorReadingId) {
    public GetSensorReadingByIdQuery {
        if (sensorReadingId == null || sensorReadingId.isBlank()) {
            throw new IllegalArgumentException("Sensor reading ID cannot be null or blank");
        }
    }
}