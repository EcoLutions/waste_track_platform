package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(String value) {
    public UserId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }
    }

    public static UserId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new UserId(value);
    }

    public static String toStringOrNull(UserId userId) {
        return userId != null ? userId.value() : null;
    }
}
