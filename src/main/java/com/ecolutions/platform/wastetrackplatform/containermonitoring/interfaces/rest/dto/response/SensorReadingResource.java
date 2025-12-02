package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record SensorReadingResource(
    String id,
    String containerId,
    Integer fillLevelPercentage,
    String temperatureCelsius,
    Integer batteryLevelPercentage,
    String validationStatus,
    String recordedAt,
    String receivedAt,
    String createdAt,
    String updatedAt
) {
}