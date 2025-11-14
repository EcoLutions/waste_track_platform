package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands;

import java.time.Duration;
import java.time.LocalTime;

public record UpdateDistrictCommand(
    String districtId,
    String name,
    String code,
    String depotLatitud,
    String depotLongitude,
    LocalTime operationStartTime,
    LocalTime operationEndTime,
    Duration maxRouteDuration
) {
    public UpdateDistrictCommand {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
    }
}