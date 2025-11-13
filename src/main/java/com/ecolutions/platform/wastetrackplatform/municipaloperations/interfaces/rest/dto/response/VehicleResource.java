package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record VehicleResource(
    String id,
    String licensePlate,
    String vehicleType,
    String volumeCapacity,
    Integer weightCapacity,
    Integer mileage,
    String districtId,
    String lastMaintenanceDate,
    String nextMaintenanceDate,
    Boolean isActive,
    String createdAt,
    String updatedAt
) {
}