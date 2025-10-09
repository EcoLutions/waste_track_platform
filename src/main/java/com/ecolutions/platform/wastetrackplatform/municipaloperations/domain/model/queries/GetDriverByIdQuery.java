package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries;

public record GetDriverByIdQuery(String driverId) {
    public GetDriverByIdQuery {
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("Driver ID cannot be null or blank");
        }
    }
}