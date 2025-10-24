package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record DistrictResource(
    String id,
    String name,
    String code,
    String boundaries,
    String operationalStatus,
    String serviceStartDate,
    String subscriptionId,
    String planName,
    Integer maxVehicles,
    Integer maxDrivers,
    Integer maxContainers,
    String subscriptionStartedAt,
    String subscriptionEndedAt,
    String primaryAdminEmail,
    String createdAt,
    String updatedAt
) {
}