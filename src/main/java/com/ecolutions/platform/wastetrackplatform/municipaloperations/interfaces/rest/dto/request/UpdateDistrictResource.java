package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record UpdateDistrictResource(
    String districtId,
    String name,
    String code
) {
    public UpdateDistrictResource {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be null or blank");
        }
    }
}