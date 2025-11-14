package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries;

import java.time.LocalDateTime;

public record ValidateDistrictScheduleTimeQuery(String districtId, LocalDateTime startedAt) {
    public ValidateDistrictScheduleTimeQuery {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (startedAt == null) {
            throw new IllegalArgumentException("Started at cannot be null");
        }
    }
}
