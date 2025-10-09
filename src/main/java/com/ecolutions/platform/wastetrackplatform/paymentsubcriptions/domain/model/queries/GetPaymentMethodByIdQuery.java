package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries;

public record GetPaymentMethodByIdQuery(String paymentMethodId) {
    public GetPaymentMethodByIdQuery {
        if (paymentMethodId == null || paymentMethodId.isBlank()) {
            throw new IllegalArgumentException("Payment method ID cannot be null or blank");
        }
    }
}