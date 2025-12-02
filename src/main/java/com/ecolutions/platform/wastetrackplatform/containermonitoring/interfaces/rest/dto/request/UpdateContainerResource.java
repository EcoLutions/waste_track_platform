package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record UpdateContainerResource(
    String containerId,
    String latitude,
    String longitude,
    Integer volumeLiters,
    Integer maxFillLevel,
    String deviceId,
    String containerType,
    String status,
    Integer collectionFrequencyDays
) {
    public UpdateContainerResource {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
    }
}