package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record DistrictId(String value) {
    public DistrictId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DistrictId cannot be null or empty");
        }
    }

    public static DistrictId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new DistrictId(value);
    }

    public static String toStringOrNull(DistrictId districtId) {
        return districtId != null ? districtId.value() : null;
    }
}
