package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record UpdateDistrictResource(
    String districtId,
    String name,
    String code,
    String depotLatitud,
    String depotLongitude,
    String disposalLatitude,
    String disposalLongitude,
    String operationStartTime,
    String operationEndTime,
    String maxRouteDuration
) {
    public UpdateDistrictResource {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
    }
}