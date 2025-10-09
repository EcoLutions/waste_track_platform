package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request;

public record UpdateSubscriptionResource(
    String planName,
    String monthlyPriceAmount,
    String monthlyPriceCurrency,
    String defaultPaymentMethodId
) {
}