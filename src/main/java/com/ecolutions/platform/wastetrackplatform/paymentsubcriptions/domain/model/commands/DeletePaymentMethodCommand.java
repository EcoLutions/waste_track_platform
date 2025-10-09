package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands;

public record DeletePaymentMethodCommand(String paymentMethodId) {
    public DeletePaymentMethodCommand {
        if (paymentMethodId == null || paymentMethodId.isBlank()) {
            throw new IllegalArgumentException("Payment method ID cannot be null or blank");
        }
    }
}