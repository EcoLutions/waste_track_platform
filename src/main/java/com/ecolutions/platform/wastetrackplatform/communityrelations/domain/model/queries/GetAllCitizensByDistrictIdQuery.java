package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries;

public record GetAllCitizensByDistrictIdQuery(String districtId) {
    public GetAllCitizensByDistrictIdQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("districtId cannot be null or blank");
        }
    }
}
