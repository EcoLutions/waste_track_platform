package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

public record DeleteDriverCommand(String driverId) {
    public DeleteDriverCommand {
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("Driver ID cannot be null or blank");
        }
    }
}