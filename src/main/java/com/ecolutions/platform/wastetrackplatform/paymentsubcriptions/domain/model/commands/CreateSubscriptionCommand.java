package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands;

public record CreateSubscriptionCommand(
    String districtId,
    String planId,
    String planName,
    String monthlyPriceAmount,
    String monthlyPriceCurrency,
    String startDate,
    String trialEndDate,
    String defaultPaymentMethodId
) {
    public CreateSubscriptionCommand {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (planId == null || planId.isBlank()) {
            throw new IllegalArgumentException("Plan ID cannot be null or blank");
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
        if (startDate == null || startDate.isBlank()) {
            throw new IllegalArgumentException("Start date cannot be null or blank");
        }
        if (trialEndDate == null || trialEndDate.isBlank()) {
            throw new IllegalArgumentException("Trial end date cannot be null or blank");
        }
    }
}