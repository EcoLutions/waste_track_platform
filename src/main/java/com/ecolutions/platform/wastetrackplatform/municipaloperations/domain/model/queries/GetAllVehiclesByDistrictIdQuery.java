package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries;

public record GetAllVehiclesByDistrictIdQuery(String districtId) {
    public GetAllVehiclesByDistrictIdQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("districtId cannot be null or blank");
        }
    }
}
