package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands;

public record CreateDeviceCommand(String deviceIdentifier) {
    public CreateDeviceCommand {
        if (deviceIdentifier == null || deviceIdentifier.isBlank()) {
            throw new IllegalArgumentException("Device identifier cannot be null or blank");
        }
    }
}
