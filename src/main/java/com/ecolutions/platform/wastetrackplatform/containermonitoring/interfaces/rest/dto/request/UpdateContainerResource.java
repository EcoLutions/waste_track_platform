package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record UpdateContainerResource(
    String containerId,
    String latitude,
    String longitude,
    Integer volumeLiters,
    Integer maxWeightKg,
    String sensorId,
    String containerType,
    String districtId,
    Integer collectionFrequencyDays
) {
    public UpdateContainerResource {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
        // Los demás campos pueden ser null para permitir actualizaciones parciales
        if (latitude != null && latitude.isBlank()) {
            throw new IllegalArgumentException("Latitude cannot be blank if provided");
        }
        if (longitude != null && longitude.isBlank()) {
            throw new IllegalArgumentException("Longitude cannot be blank if provided");
        }
        if (volumeLiters != null && volumeLiters <= 0) {
            throw new IllegalArgumentException("Volume liters must be greater than zero if provided");
        }
        if (maxWeightKg != null && maxWeightKg <= 0) {
            throw new IllegalArgumentException("Max weight kg must be greater than zero if provided");
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