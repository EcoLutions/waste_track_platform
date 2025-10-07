package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateDeliveryAttemptResource(
    @NotBlank String deliveryAttemptId,
    @NotBlank String requestId,
    @NotBlank String channel,
    @NotBlank String provider,
    @NotBlank String providerMessageId,
    @NotBlank String status,
    @NotNull Integer attemptNumber,
    @NotNull Boolean canRetry,
    @NotBlank String sentAt,
    String deliveredAt,
    String errorCode,
    String errorMessage,
    @NotBlank String costAmount,
    @NotBlank String costCurrency
) {
    public UpdateDeliveryAttemptResource {
        if (deliveryAttemptId == null || deliveryAttemptId.isBlank()) {
            throw new IllegalArgumentException("Delivery Attempt ID cannot be null or blank");
        }
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("Request ID cannot be null or blank");
        }
        if (channel == null || channel.isBlank()) {
            throw new IllegalArgumentException("Channel cannot be null or blank");
        }
        if (provider == null || provider.isBlank()) {
            throw new IllegalArgumentException("Provider cannot be null or blank");
        }
        if (providerMessageId == null || providerMessageId.isBlank()) {
            throw new IllegalArgumentException("Provider Message ID cannot be null or blank");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or blank");
        }
        if (attemptNumber == null) {
            throw new IllegalArgumentException("Attempt Number cannot be null");
        }
        if (canRetry == null) {
            throw new IllegalArgumentException("Can Retry cannot be null");
        }
        if (sentAt == null || sentAt.isBlank()) {
            throw new IllegalArgumentException("Sent At cannot be null or blank");
        }
        if (costAmount == null || costAmount.isBlank()) {
            throw new IllegalArgumentException("Cost Amount cannot be null or blank");
        }
        if (costCurrency == null || costCurrency.isBlank()) {
            throw new IllegalArgumentException("Cost Currency cannot be null or blank");
        }
    }
}