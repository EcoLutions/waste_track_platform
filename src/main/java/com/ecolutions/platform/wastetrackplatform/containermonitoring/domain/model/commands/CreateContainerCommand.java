package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands;

public record CreateContainerCommand(
    // Geographical Coordinates
    String latitude,
    String longitude,
    // Physical Characteristics
    Integer volumeLiters,
    Integer maxFillLevel,
    // Sensor identification which is optional
    String deviceId,
    // Operational Details
    String containerType,
    String districtId,
    Integer collectionFrequencyDays
) {
    public CreateContainerCommand {
        if (latitude == null || latitude.isBlank()) {
            throw new IllegalArgumentException("Latitude cannot be null or blank");
        }
        if (longitude == null || longitude.isBlank()) {
            throw new IllegalArgumentException("Longitude cannot be null or blank");
        }
        if (volumeLiters == null || volumeLiters <= 0) {
            throw new IllegalArgumentException("Volume liters cannot be null or less than or equal to zero");
        }
        if (maxFillLevel == null || maxFillLevel <= 0) {
            throw new IllegalArgumentException("Max weight kg cannot be null or less than or equal to zero");
        }
        if (containerType == null || containerType.isBlank()) {
            throw new IllegalArgumentException("Container type cannot be null or blank");
        }
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (collectionFrequencyDays == null || collectionFrequencyDays <= 0) {
            throw new IllegalArgumentException("Collection frequency days cannot be null or less than or equal to zero");
        }
    }
}