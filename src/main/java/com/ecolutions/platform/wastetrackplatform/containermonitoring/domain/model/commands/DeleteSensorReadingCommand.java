package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands;

public record DeleteSensorReadingCommand(String sensorReadingId) {
    public DeleteSensorReadingCommand {
        if (sensorReadingId == null || sensorReadingId.isBlank()) {
            throw new IllegalArgumentException("Sensor reading ID cannot be null or blank");
        }
    }
}