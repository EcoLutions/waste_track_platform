package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record DeviceIdentifier(String value) {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 64;
    private static final String PATTERN = "^[A-Za-z0-9-_]+$"; // Ajustable según tus devices

    public DeviceIdentifier {
        if (value == null)
            throw new IllegalArgumentException("DeviceIdentifier cannot be null");
        var normalized = value.trim();
        if (normalized.isEmpty())
            throw new IllegalArgumentException("DeviceIdentifier cannot be blank");
        if (normalized.length() < MIN_LENGTH || normalized.length() > MAX_LENGTH)
            throw new IllegalArgumentException("DeviceIdentifier length must be between %d and %d characters".formatted(MIN_LENGTH, MAX_LENGTH));
        if (!normalized.matches(PATTERN))
            throw new IllegalArgumentException("DeviceIdentifier contains invalid characters. Allowed: letters, numbers, '-', '_'");
        value = normalized;
    }

    public static DeviceIdentifier of(String value) {
        return new DeviceIdentifier(value);
    }

    public static String toStringOrNull(DeviceIdentifier deviceIdentifier) {
        return deviceIdentifier != null ? deviceIdentifier.value() : null;
    }
}
