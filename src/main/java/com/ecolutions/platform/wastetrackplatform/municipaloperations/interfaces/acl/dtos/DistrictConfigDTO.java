package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

public record DistrictConfigDTO(
        String districtId,
        String name,
        Duration maxRouteDuration,
        LocalTime operationStartTime,
        LocalTime operationEndTime,
        BigDecimal depotLatitude,
        BigDecimal depotLongitude,
        BigDecimal disposalLatitude,
        BigDecimal disposalLongitude
) {
}
