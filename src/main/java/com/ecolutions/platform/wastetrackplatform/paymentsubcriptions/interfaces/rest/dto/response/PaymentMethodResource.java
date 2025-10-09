package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record PaymentMethodResource(
    String id,
    String districtId,
    String type,
    String culqiTokenId,
    String cardBrand,
    String lastFourDigits,
    Integer expiryMonth,
    Integer expiryYear,
    Boolean isDefault,
    Boolean isValid,
    String registeredAt,
    String lastUsedAt
) {
}