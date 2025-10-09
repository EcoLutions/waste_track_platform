package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record SubscriptionResource(
    String id,
    String districtId,
    String planId,
    String planName,
    String monthlyPriceAmount,
    String monthlyPriceCurrency,
    String status,
    String startDate,
    String trialEndDate,
    String currentPeriodStart,
    String currentPeriodEnd,
    String nextBillingDate,
    String gracePeriodEndDate,
    String cancelledAt,
    String defaultPaymentMethodId
) {
}