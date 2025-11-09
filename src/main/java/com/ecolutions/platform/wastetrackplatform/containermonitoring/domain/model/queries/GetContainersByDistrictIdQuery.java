package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries;

public record GetContainersByDistrictIdQuery(String districtId) {
    public GetContainersByDistrictIdQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
    }
}