package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record CreateSensorReadingResource(
    String containerId,
    Integer fillLevelPercentage,
    Double temperatureCelsius,
    Integer batteryLevelPercentage
) {
    public CreateSensorReadingResource {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
        if (fillLevelPercentage == null || fillLevelPercentage < 0 || fillLevelPercentage > 100) {
            throw new IllegalArgumentException("Fill level percentage must be between 0 and 100");
        }
        if (temperatureCelsius == null || temperatureCelsius < -50 || temperatureCelsius > 100) {
            throw new IllegalArgumentException("Temperature must be between -50°C and 100°C");
        }
        if (batteryLevelPercentage == null || batteryLevelPercentage < 0 || batteryLevelPercentage > 100) {
            throw new IllegalArgumentException("Battery level percentage must be between 0 and 100");
        }
    }
}