package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries;

public record GetVehicleByIdQuery(String vehicleId) {
    public GetVehicleByIdQuery {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or blank");
        }
    }
}