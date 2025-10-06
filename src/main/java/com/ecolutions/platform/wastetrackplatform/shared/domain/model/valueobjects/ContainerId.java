package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record ContainerId(String value) {
    public ContainerId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ContainerId cannot be null or empty");
        }
    }

    public static ContainerId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new ContainerId(value);
    }

    public static String toStringOrNull(ContainerId containerId) {
        return containerId != null ? containerId.value() : null;
    }
}
