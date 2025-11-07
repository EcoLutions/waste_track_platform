package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

public record UpdateDistrictCommand(
    String districtId,
    String name,
    String code
) {
    public UpdateDistrictCommand {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be null or blank");
        }
    }
}