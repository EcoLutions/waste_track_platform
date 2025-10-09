package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries;

public record GetPlanCatalogByIdQuery(String planCatalogId) {
    public GetPlanCatalogByIdQuery {
        if (planCatalogId == null || planCatalogId.isBlank()) {
            throw new IllegalArgumentException("Plan catalog ID cannot be null or blank");
        }
    }
}