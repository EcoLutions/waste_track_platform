package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record CitizenId(String value) {
    public CitizenId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CitizenId cannot be null or empty");
        }
    }

    public static CitizenId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new CitizenId(value);
    }

    public static String toStringOrNull(CitizenId citizenId) {
        return citizenId != null ? citizenId.value() : null;
    }
}
