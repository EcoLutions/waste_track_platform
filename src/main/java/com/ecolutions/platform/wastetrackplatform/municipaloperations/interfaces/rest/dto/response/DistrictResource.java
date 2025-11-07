package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record DistrictResource(
    String id,
    String name,
    String code,
    String operationalStatus,
    String serviceStartDate,
    String planId,
    String planName,
    Integer maxVehicles,
    Integer maxDrivers,
    Integer maxContainers,
    String currency,
    String price,
    String billingPeriod,
    Integer currentVehicleCount,
    Integer currentDriverCount,
    Integer currentContainerCount,
    String createdAt,
    String updatedAt
) {
}