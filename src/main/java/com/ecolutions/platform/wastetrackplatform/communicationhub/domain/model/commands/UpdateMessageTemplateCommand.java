package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

import java.util.List;

public record UpdateMessageTemplateCommand(
    String messageTemplateId,
    String name,
    String category,
    List<String> supportedChannels,
    String emailSubject,
    String emailBody,
    String smsBody,
    String pushTitle,
    String pushBody,
    List<String> variables,
    Boolean isActive
) {
    public UpdateMessageTemplateCommand {
        if (messageTemplateId == null || messageTemplateId.isBlank()) {
            throw new IllegalArgumentException("Message Template ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be null or blank");
        }
        if (supportedChannels == null || supportedChannels.isEmpty()) {
            throw new IllegalArgumentException("Supported Channels cannot be null or empty");
        }
        if (isActive == null) {
            throw new IllegalArgumentException("Is Active cannot be null");
        }
    }
}