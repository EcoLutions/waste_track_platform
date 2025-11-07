package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands;

public record CreatePlanCatalogCommand(
    String name,
    String priceAmount,
    String billingPeriod,
    Integer maxVehicles,
    Integer maxDrivers,
    Integer maxContainers
) {
    public CreatePlanCatalogCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (priceAmount == null || priceAmount.isBlank()) {
            throw new IllegalArgumentException("Monthly price amount cannot be null or blank");
        }
        if (billingPeriod == null || billingPeriod.isBlank()) {
            throw new IllegalArgumentException("Billing period cannot be null or blank");
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