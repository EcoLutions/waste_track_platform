package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdatePaymentMethodResource(
    @NotNull(message = "Type is required")
    @NotBlank(message = "Type cannot be blank")
    String type,

    @NotNull(message = "Culqi token ID is required")
    @NotBlank(message = "Culqi token ID cannot be blank")
    String culqiTokenId,

    @NotNull(message = "Card brand is required")
    @NotBlank(message = "Card brand cannot be blank")
    String cardBrand,

    @NotNull(message = "Last four digits are required")
    @NotBlank(message = "Last four digits cannot be blank")
    String lastFourDigits,

    @NotNull(message = "Expiry month is required")
    @Positive(message = "Expiry month must be positive")
    Integer expiryMonth,

    @NotNull(message = "Expiry year is required")
    @Positive(message = "Expiry year must be positive")
    Integer expiryYear
) {
}