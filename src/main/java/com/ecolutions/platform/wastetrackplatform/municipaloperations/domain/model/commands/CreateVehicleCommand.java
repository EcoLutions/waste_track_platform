package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

import java.math.BigDecimal;

public record CreateVehicleCommand(
    String licensePlate,
    String vehicleType,
    BigDecimal volumeCapacity,
    BigDecimal weightCapacity,
    String districtId
) {
    public CreateVehicleCommand {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate cannot be null or blank");
        }
        if (vehicleType == null || vehicleType.isBlank()) {
            throw new IllegalArgumentException("Vehicle type cannot be null or blank");
        }
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (volumeCapacity != null && volumeCapacity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Volume capacity must be greater than zero");
        }
        if (weightCapacity != null && weightCapacity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Weight capacity must be greater than zero");
        }
    }
}