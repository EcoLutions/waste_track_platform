package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries;

public record GetReportByIdQuery(String reportId) {
    public GetReportByIdQuery {
        if (reportId == null || reportId.isBlank()) {
            throw new IllegalArgumentException("Report ID cannot be null or blank");
        }
    }
}