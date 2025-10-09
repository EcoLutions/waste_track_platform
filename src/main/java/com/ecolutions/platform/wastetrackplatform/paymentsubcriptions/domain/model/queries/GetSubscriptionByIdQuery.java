package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries;

public record GetSubscriptionByIdQuery(String subscriptionId) {
    public GetSubscriptionByIdQuery {
        if (subscriptionId == null || subscriptionId.isBlank()) {
            throw new IllegalArgumentException("Subscription ID cannot be null or blank");
        }
    }
}