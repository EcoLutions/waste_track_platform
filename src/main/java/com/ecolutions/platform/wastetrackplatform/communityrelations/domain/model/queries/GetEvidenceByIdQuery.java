package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries;

public record GetEvidenceByIdQuery(String id) {
    public GetEvidenceByIdQuery {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Evidence ID cannot be null or blank");
        }
    }
}