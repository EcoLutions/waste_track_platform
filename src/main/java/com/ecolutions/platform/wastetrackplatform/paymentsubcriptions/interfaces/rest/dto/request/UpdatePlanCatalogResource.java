package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request;

public record UpdatePlanCatalogResource(
    String name,
    String monthlyPriceAmount,
    String monthlyPriceCurrency,
    Integer maxVehicles,
    Integer maxDrivers,
    Integer maxContainers
) {
}