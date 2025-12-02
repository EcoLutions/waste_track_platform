package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request;

public record CreatePlanCatalogResource(
    String name,
    String priceAmount,
    String billingPeriod,
    Integer maxVehicles,
    Integer maxDrivers,
    Integer maxContainers
) {
}