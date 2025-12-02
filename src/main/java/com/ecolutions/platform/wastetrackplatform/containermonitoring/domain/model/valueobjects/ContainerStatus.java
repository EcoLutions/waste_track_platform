package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

public enum ContainerStatus {
    ACTIVE,
    MAINTENANCE,
    DECOMMISSIONED;

    public static ContainerStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ContainerStatus cannot be null or blank");
        }
        try {
            return ContainerStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ContainerStatus: " + value);
        }
    }

    public static String toStringOrNull(ContainerStatus containerStatus) {
        return containerStatus != null ? containerStatus.name() : null;
    }
}