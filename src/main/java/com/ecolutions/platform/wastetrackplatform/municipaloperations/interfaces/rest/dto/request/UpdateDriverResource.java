package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request;

import java.time.LocalDate;

public record UpdateDriverResource(
    String driverId,
    String districtId,
    String firstName,
    String lastName,
    String documentNumber,
    String phoneNumber,
    String userId,
    String driverLicense,
    LocalDate licenseExpiryDate,
    String emailAddress
) {
    public UpdateDriverResource {
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("Driver ID cannot be null or blank");
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
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new IllegalArgumentException("Document number cannot be null or blank");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (driverLicense == null || driverLicense.isBlank()) {
            throw new IllegalArgumentException("Driver license cannot be null or blank");
        }
        if (licenseExpiryDate == null) {
            throw new IllegalArgumentException("License expiry date cannot be null");
        }
        if (emailAddress == null || emailAddress.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be null or blank");
        }
    }
}