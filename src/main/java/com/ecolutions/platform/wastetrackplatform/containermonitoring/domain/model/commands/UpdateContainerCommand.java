package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands;

public record UpdateContainerCommand(
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
    public UpdateContainerCommand {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
    }
}