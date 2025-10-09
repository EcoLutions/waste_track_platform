package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands;

public record UpdateSubscriptionCommand(
    String subscriptionId,
    String planName,
    String monthlyPriceAmount,
    String monthlyPriceCurrency,
    String defaultPaymentMethodId
) {
    public UpdateSubscriptionCommand {
        if (subscriptionId == null || subscriptionId.isBlank()) {
            throw new IllegalArgumentException("Subscription ID cannot be null or blank");
        }
        if (planName == null || planName.isBlank()) {
            throw new IllegalArgumentException("Plan name cannot be null or blank");
        }
        if (monthlyPriceAmount == null || monthlyPriceAmount.isBlank()) {
            throw new IllegalArgumentException("Monthly price amount cannot be null or blank");
        }
        if (monthlyPriceCurrency == null || monthlyPriceCurrency.isBlank()) {
            throw new IllegalArgumentException("Monthly price currency cannot be null or blank");
        }
    }
}