package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries;

public record GetAllDriversByDistrictIdQuery(String districtId) {
    public GetAllDriversByDistrictIdQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("districtId cannot be null or blank");
        }
    }
}
