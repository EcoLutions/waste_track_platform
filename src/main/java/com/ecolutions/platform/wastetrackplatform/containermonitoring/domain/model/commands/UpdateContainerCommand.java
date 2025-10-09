package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands;

public record UpdateContainerCommand(
    String containerId,
    String latitude,
    String longitude,
    String address,
    String districtCode,
    Integer volumeLiters,
    Integer maxWeightKg,
    String sensorId,
    String containerType,
    String districtId,
    Integer collectionFrequencyDays
) {
    public UpdateContainerCommand {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
        if (latitude != null && latitude.isBlank()) {
            throw new IllegalArgumentException("Latitude cannot be blank if provided");
        }
        if (longitude != null && longitude.isBlank()) {
            throw new IllegalArgumentException("Longitude cannot be blank if provided");
        }
        if (address != null && address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be blank if provided");
        }
        if (districtCode != null && districtCode.isBlank()) {
            throw new IllegalArgumentException("District code cannot be blank if provided");
        }
        if (volumeLiters != null && volumeLiters <= 0) {
            throw new IllegalArgumentException("Volume liters must be greater than zero if provided");
        }
        if (maxWeightKg != null && maxWeightKg <= 0) {
            throw new IllegalArgumentException("Max weight kg must be greater than zero if provided");
        }
        if (sensorId != null && sensorId.isBlank()) {
            throw new IllegalArgumentException("Sensor ID cannot be blank if provided");
        }
        if (containerType != null && containerType.isBlank()) {
            throw new IllegalArgumentException("Container type cannot be blank if provided");
        }
        if (districtId != null && districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be blank if provided");
        }
        if (collectionFrequencyDays != null && collectionFrequencyDays <= 0) {
            throw new IllegalArgumentException("Collection frequency days must be greater than zero if provided");
        }
    }
}