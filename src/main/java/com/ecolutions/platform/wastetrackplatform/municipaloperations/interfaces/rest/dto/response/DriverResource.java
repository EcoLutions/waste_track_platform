package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record DriverResource(
    String id,
    String districtId,
    String firstName,
    String lastName,
    String documentNumber,
    String phoneNumber,
    String userId,
    String driverLicense,
    String licenseExpiryDate,
    String emailAddress,
    Integer totalHoursWorked,
    String lastRouteCompletedAt,
    String status,
    String createdAt,
    String updatedAt
) {
}