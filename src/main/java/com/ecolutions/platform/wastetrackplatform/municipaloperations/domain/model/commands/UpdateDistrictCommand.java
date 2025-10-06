package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.GeographicBoundaries;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;

public record UpdateDistrictCommand(
    String districtId,
    String name,
    String code,
    GeographicBoundaries boundaries,
    EmailAddress primaryAdminEmail
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
        if (boundaries == null) {
            throw new IllegalArgumentException("Boundaries cannot be null");
        }
        if (primaryAdminEmail == null) {
            throw new IllegalArgumentException("Primary admin email cannot be null");
        }
    }
}