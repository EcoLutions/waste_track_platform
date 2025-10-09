package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands;

public record CreatePaymentMethodCommand(
    String districtId,
    String type,
    String culqiTokenId,
    String cardBrand,
    String lastFourDigits,
    Integer expiryMonth,
    Integer expiryYear
) {
    public CreatePaymentMethodCommand {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type cannot be null or blank");
        }
        if (culqiTokenId == null || culqiTokenId.isBlank()) {
            throw new IllegalArgumentException("Culqi token ID cannot be null or blank");
        }
        if (expiryMonth == null || expiryMonth < 1 || expiryMonth > 12) {
            throw new IllegalArgumentException("Expiry month must be between 1 and 12");
        }
        if (expiryYear == null || expiryYear < 2024) {
            throw new IllegalArgumentException("Expiry year must be 2024 or later");
        }
    }
}