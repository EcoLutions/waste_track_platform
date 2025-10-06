package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record DriverId(String value) {
    public DriverId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DriverId cannot be null or empty");
        }
    }

    public static DriverId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new DriverId(value);
    }

    public static String toStringOrNull(DriverId driverId) {
        return driverId != null ? driverId.value() : null;
    }
}
