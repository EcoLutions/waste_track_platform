package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

import java.math.BigDecimal;

public record UpdateVehicleCommand(
    String vehicleId,
    String licensePlate,
    String vehicleType,
    BigDecimal volumeCapacity,
    BigDecimal weightCapacity,
    String districtId,
    String assignedDriverId,
    String lastMaintenanceDate,
    String nextMaintenanceDate,
    Boolean isActive
) {
    public UpdateVehicleCommand {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or blank");
        }
        if (licensePlate != null && licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate cannot be blank");
        }
        if (vehicleType != null && vehicleType.isBlank()) {
            throw new IllegalArgumentException("Vehicle type cannot be blank");
        }
        if (districtId != null && districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be blank");
        }
        if (assignedDriverId != null && assignedDriverId.isBlank()) {
            throw new IllegalArgumentException("Assigned driver ID cannot be blank");
        }
        if (volumeCapacity != null && volumeCapacity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Volume capacity must be greater than zero");
        }
        if (weightCapacity != null && weightCapacity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Weight capacity must be greater than zero");
        }
    }
}