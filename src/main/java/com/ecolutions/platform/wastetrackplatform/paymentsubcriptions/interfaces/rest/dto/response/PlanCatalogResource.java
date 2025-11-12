package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record PlanCatalogResource(
    String id,
    String name,
    String priceAmount,
    String priceCurrency,
    String billingPeriod,
    Integer maxVehicles,
    Integer maxDrivers,
    Integer maxContainers
) {
}