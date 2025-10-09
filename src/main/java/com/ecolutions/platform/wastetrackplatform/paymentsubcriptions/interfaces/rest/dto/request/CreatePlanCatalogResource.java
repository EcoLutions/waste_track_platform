package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request;

public record CreatePlanCatalogResource(
    String name,
    String monthlyPriceAmount,
    String monthlyPriceCurrency,
    Integer maxVehicles,
    Integer maxDrivers,
    Integer maxContainers
) {
}