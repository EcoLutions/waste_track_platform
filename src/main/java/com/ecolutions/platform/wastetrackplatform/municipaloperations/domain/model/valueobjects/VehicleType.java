package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

public enum VehicleType {
    COMPACTOR,
    TRUCK,
    MINI_TRUCK;

    public static VehicleType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("VehicleType cannot be null or blank");
        }
        try {
            return VehicleType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid VehicleType: " + value);
        }
    }
}