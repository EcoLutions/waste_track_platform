package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateDistrictResource(
    @NotBlank
    String districtId,

    @NotBlank
    String name,

    @NotBlank
    String code,

    @NotBlank
    String boundaries,

    @NotBlank
    String primaryAdminEmail
) {
    public UpdateDistrictResource {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be null or blank");
        }
        if (boundaries == null || boundaries.isBlank()) {
            throw new IllegalArgumentException("Boundaries cannot be null or blank");
        }
        if (primaryAdminEmail == null || primaryAdminEmail.isBlank()) {
            throw new IllegalArgumentException("Primary admin email cannot be null or blank");
        }
    }
}