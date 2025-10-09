package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record ContainerResource(
    String id,
    String latitude,
    String longitude,
    String address,
    String districtCode,
    Integer volumeLiters,
    Integer maxWeightKg,
    String containerType,
    String status,
    Integer currentFillLevel,
    String sensorId,
    String lastReadingTimestamp,
    String districtId,
    String lastCollectionDate,
    Integer collectionFrequencyDays,
    String createdAt,
    String updatedAt
) {
}