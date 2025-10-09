package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands;

public record UpdatePlanCatalogCommand(
    String planCatalogId,
    String name,
    String monthlyPriceAmount,
    String monthlyPriceCurrency,
    Integer maxVehicles,
    Integer maxDrivers,
    Integer maxContainers
) {
    public UpdatePlanCatalogCommand {
        if (planCatalogId == null || planCatalogId.isBlank()) {
            throw new IllegalArgumentException("Plan catalog ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (monthlyPriceAmount == null || monthlyPriceAmount.isBlank()) {
            throw new IllegalArgumentException("Monthly price amount cannot be null or blank");
        }
        if (monthlyPriceCurrency == null || monthlyPriceCurrency.isBlank()) {
            throw new IllegalArgumentException("Monthly price currency cannot be null or blank");
        }
        if (maxVehicles == null || maxVehicles <= 0) {
            throw new IllegalArgumentException("Max vehicles must be greater than 0");
        }
        if (maxDrivers == null || maxDrivers <= 0) {
            throw new IllegalArgumentException("Max drivers must be greater than 0");
        }
        if (maxContainers == null || maxContainers <= 0) {
            throw new IllegalArgumentException("Max containers must be greater than 0");
        }
    }
}