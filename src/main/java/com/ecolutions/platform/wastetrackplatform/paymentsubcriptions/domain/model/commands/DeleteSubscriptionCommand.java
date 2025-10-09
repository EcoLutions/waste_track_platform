package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands;

public record DeleteSubscriptionCommand(String subscriptionId) {
    public DeleteSubscriptionCommand {
        if (subscriptionId == null || subscriptionId.isBlank()) {
            throw new IllegalArgumentException("Subscription ID cannot be null or blank");
        }
    }
}