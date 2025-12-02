package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.dtos;

import java.math.BigDecimal;

public record PlanInfoDTO(
        String planId,
        String planName,
        Integer maxVehicles,
        Integer maxDrivers,
        Integer maxContainers,
        BigDecimal priceAmount,
        String currency,
        String billingPeriod
) {
}
