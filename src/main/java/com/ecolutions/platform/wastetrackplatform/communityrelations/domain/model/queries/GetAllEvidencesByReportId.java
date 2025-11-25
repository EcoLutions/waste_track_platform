package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries;

public record GetAllEvidencesByReportId(String reportId) {
    public GetAllEvidencesByReportId {
        if (reportId == null || reportId.isBlank()) {
            throw new IllegalArgumentException("reportId cannot be null or blank");
        }
    }
}
