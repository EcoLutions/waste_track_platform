package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands;

public record DeletePlanCatalogCommand(String planCatalogId) {
    public DeletePlanCatalogCommand {
        if (planCatalogId == null || planCatalogId.isBlank()) {
            throw new IllegalArgumentException("Plan catalog ID cannot be null or blank");
        }
    }
}