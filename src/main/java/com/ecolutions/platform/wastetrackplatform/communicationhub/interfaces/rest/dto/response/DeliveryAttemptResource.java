package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DeliveryAttemptResource(
    String id,
    String requestId,
    String channel,
    String provider,
    String providerMessageId,
    String status,
    Integer attemptNumber,
    Boolean canRetry,
    LocalDateTime sentAt,
    LocalDateTime deliveredAt,
    String errorCode,
    String errorMessage,
    String costAmount,
    String costCurrency
) {
}