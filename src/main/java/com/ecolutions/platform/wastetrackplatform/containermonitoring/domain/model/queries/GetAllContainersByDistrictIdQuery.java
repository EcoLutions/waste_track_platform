package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries;

public record GetAllContainersByDistrictIdQuery(String districtId) {
    public GetAllContainersByDistrictIdQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
    }
}
