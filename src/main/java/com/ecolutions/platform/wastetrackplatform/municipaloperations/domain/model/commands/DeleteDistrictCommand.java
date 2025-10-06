package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

public record DeleteDistrictCommand(String districtId) {
    public DeleteDistrictCommand {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
    }
}