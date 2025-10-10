package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries;

public record GetContainersInAlertByDistrictIdQuery(String districtId) {
    public GetContainersInAlertByDistrictIdQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
    }
}