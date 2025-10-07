package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.NotificationRequestId;

public record CreateDeliveryAttemptCommand(
    String requestId,
    String channel,
    String provider,
    String providerMessageId,
    String status,
    Integer attemptNumber,
    Boolean canRetry,
    String sentAt,
    String deliveredAt,
    String errorCode,
    String errorMessage,
    String costAmount,
    String costCurrency
) {
    public CreateDeliveryAttemptCommand {
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