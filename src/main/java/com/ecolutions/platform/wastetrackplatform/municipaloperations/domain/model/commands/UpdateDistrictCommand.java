package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

public record UpdateDistrictCommand(
    String districtId,
    String name,
    String code,
    String depotLatitud,
    String depotLongitude
) {
    public UpdateDistrictCommand {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
    }
}