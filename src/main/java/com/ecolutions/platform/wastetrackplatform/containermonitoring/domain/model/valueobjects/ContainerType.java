package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

public enum ContainerType {
    ORGANIC,
    RECYCLABLE,
    GENERAL;

    public static ContainerType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ContainerType cannot be null or blank");
        }
        try {
            return ContainerType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ContainerType: " + value);
        }
    }
}