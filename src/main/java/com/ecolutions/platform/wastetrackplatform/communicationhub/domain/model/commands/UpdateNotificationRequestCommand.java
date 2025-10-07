package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record UpdateNotificationRequestCommand(
    String notificationRequestId,
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
    LocalDateTime scheduledFor,
    LocalDateTime expiresAt
) {
    public UpdateNotificationRequestCommand {
        if (notificationRequestId == null || notificationRequestId.isBlank()) {
            throw new IllegalArgumentException("NotificationRequest ID cannot be null or blank");
        }
        if (sourceContext == null || sourceContext.isBlank()) {
            throw new IllegalArgumentException("SourceContext cannot be null or blank");
        }
        if (recipientId == null || recipientId.isBlank()) {
            throw new IllegalArgumentException("Recipient ID cannot be null or blank");
        }
        if (recipientType == null || recipientType.isBlank()) {
            throw new IllegalArgumentException("Recipient type cannot be null or blank");
        }
        if (recipientEmail == null || recipientEmail.isBlank()) {
            throw new IllegalArgumentException("Recipient email cannot be null or blank");
        }
        if (recipientPhone == null || recipientPhone.isBlank()) {
            throw new IllegalArgumentException("Recipient phone cannot be null or blank");
        }
        if (messageType == null || messageType.isBlank()) {
            throw new IllegalArgumentException("Message type cannot be null or blank");
        }
        if (templateId == null || templateId.isBlank()) {
            throw new IllegalArgumentException("Template ID cannot be null or blank");
        }
        if (channels == null || channels.isEmpty()) {
            throw new IllegalArgumentException("Channels cannot be null or empty");
        }
        if (priority == null || priority.isBlank()) {
            throw new IllegalArgumentException("Priority cannot be null or blank");
        }
        if (scheduledFor == null) {
            throw new IllegalArgumentException("Scheduled for cannot be null");
        }
        if (expiresAt == null) {
            throw new IllegalArgumentException("Expires at cannot be null");
        }
    }
}