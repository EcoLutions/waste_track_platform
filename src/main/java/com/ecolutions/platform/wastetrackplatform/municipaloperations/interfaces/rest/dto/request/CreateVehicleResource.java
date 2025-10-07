package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request;

import java.math.BigDecimal;

public record CreateVehicleResource(
    String licensePlate,
    String vehicleType,
    String volumeCapacity,
    String weightCapacity,
    String districtId
) {
    public CreateVehicleResource {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate cannot be null or blank");
        }
        if (vehicleType == null || vehicleType.isBlank()) {
            throw new IllegalArgumentException("Vehicle type cannot be null or blank");
        }
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (volumeCapacity == null || volumeCapacity.isBlank()) {
            throw new IllegalArgumentException("Volume capacity cannot be null or blank");
        }
        if (weightCapacity == null || weightCapacity.isBlank()) {
            throw new IllegalArgumentException("Weight capacity cannot be null or blank");
        }

        try {
            BigDecimal volume = new BigDecimal(volumeCapacity);
            if (volume.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Volume capacity must be a positive number");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Volume capacity must be a valid number", e);
        }

        try {
            BigDecimal weight = new BigDecimal(weightCapacity);
            if (weight.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Weight capacity must be a positive number");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Weight capacity must be a valid number", e);
        }
    }
}