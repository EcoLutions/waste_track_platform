package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries;

public record GetDistrictByIdQuery(String districtId) {
    public GetDistrictByIdQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
    }
}