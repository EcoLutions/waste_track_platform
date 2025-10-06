package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record RoleId(String value) {
    public RoleId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RoleId cannot be null or empty");
        }
    }

    public static RoleId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new RoleId(value);
    }

    public static String toStringOrNull(RoleId roleId) {
        return roleId != null ? roleId.value() : null;
    }
}
