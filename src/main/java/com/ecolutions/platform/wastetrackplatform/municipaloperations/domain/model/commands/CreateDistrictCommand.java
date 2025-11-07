package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Username;

public record CreateDistrictCommand(
    String name,
    String code,
    EmailAddress primaryAdminEmail,
    Username primaryAdminUsername,
    String planId
) {
    public CreateDistrictCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be null or blank");
        }
        if (primaryAdminEmail == null) {
            throw new IllegalArgumentException("Primary admin email cannot be null");
        }
        if (primaryAdminUsername == null) {
            throw new IllegalArgumentException("Primary admin username cannot be null");
        }
        if (planId == null || planId.isBlank()) {
            throw new IllegalArgumentException("Plan ID cannot be null or blank");
        }
    }
}