package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries;

public record GetCitizenByIdQuery(String citizenId) {
    public GetCitizenByIdQuery {
        if (citizenId == null || citizenId.isBlank()) {
            throw new IllegalArgumentException("Citizen ID cannot be null or blank");
        }
    }
}