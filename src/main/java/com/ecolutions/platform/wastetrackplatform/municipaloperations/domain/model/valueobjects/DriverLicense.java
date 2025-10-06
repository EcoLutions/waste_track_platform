package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record DriverLicense(String licenseNumber) {
    public DriverLicense {
        if (licenseNumber == null || licenseNumber.isBlank()) {
            throw new IllegalArgumentException("License number cannot be null or blank");
        }
        if (!isValidPeruvianLicenseNumber(licenseNumber)) {
            throw new IllegalArgumentException("Invalid Peruvian driver license number format");
        }
    }

    private static boolean isValidPeruvianLicenseNumber(String licenseNumber) {
        // 8 digits or 1 letter followed by 7 digits
        return licenseNumber.matches("[A-Za-z]\\d{7}") || licenseNumber.matches("\\d{8}");
    }
}