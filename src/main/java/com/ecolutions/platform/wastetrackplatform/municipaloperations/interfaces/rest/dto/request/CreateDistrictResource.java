package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request;

public record CreateDistrictResource(
    String name,
    String code,
    String primaryAdminEmail,
    String primaryAdminUsername,
    String planId
) {
    public CreateDistrictResource {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be null or blank");
        }
        if (primaryAdminEmail == null || primaryAdminEmail.isBlank()) {
            throw new IllegalArgumentException("Primary admin email cannot be null or blank");
        }
        if (planId == null || planId.isBlank()) {
            throw new IllegalArgumentException("Plan ID cannot be null or blank");
        }
    }
}