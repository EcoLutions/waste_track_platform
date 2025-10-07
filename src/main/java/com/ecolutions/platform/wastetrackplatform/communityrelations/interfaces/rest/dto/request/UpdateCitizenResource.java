package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record UpdateCitizenResource(
    String citizenId,
    String districtId,
    String firstName,
    String lastName,
    String email,
    String phoneNumber
) {
    public UpdateCitizenResource {
        if (citizenId == null || citizenId.isBlank()) {
            throw new IllegalArgumentException("Citizen ID cannot be null or blank");
        }
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }
    }
}