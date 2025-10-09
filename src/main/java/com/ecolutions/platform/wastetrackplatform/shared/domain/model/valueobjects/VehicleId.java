package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record VehicleId(String value) {
    public VehicleId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("VehicleId cannot be null or empty");
        }
    }

    public static VehicleId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new VehicleId(value);
    }

    public static String toStringOrNull(VehicleId vehicleId) {
        return vehicleId != null ? vehicleId.value() : null;
    }
}
