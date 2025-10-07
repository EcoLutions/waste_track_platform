package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

public record DeleteVehicleCommand(String vehicleId) {
    public DeleteVehicleCommand {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or blank");
        }
    }
}