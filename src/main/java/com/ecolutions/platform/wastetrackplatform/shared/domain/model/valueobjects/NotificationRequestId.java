package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record NotificationRequestId(String value) {
    public NotificationRequestId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("NotificationRequestId cannot be null or empty");
        }
    }

    public static NotificationRequestId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new NotificationRequestId(value);
    }

    public static String toStringOrNull(NotificationRequestId notificationRequestId) {
        return notificationRequestId != null ? notificationRequestId.value() : null;
    }
}
