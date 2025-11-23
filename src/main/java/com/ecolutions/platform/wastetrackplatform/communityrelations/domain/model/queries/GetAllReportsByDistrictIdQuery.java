package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries;

public record GetAllReportsByDistrictIdQuery(String districtId) {
    public GetAllReportsByDistrictIdQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("districtId cannot be null or blank");
        }
    }
}
