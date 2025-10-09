package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record NotificationRequestResource(
    String id,
    String sourceContext,
    String recipientId,
    String recipientType,
    String recipientEmail,
    String recipientPhone,
    String messageType,
    String templateId,
    Map<String, String> templateData,
    List<String> channels,
    String priority,
    String scheduledFor,
    String expiresAt,
    String status,
    String sentAt,
    String failureReason
) {
}