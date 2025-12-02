package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

public record DeviceId(String value) {
    public DeviceId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DeviceId cannot be null or empty");
        }
    }

    public static DeviceId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new DeviceId(value);
    }

    public static String toStringOrNull(DeviceId deviceId) {
        return deviceId != null ? deviceId.value() : null;
    }
}
