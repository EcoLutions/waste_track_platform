package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record PlanCatalogResource(
    String id,
    String name,
    String monthlyPriceAmount,
    String monthlyPriceCurrency,
    Integer maxVehicles,
    Integer maxDrivers,
    Integer maxContainers
) {
}