package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request;

public record CreateSubscriptionResource(
    String districtId,
    String planId,
    String planName,
    String monthlyPriceAmount,
    String monthlyPriceCurrency,
    String startDate,
    String trialEndDate,
    String defaultPaymentMethodId
) {
}