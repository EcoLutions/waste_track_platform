package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

public record DeleteMessageTemplateCommand(String messageTemplateId) {
    public DeleteMessageTemplateCommand {
        if (messageTemplateId == null || messageTemplateId.isBlank()) {
            throw new IllegalArgumentException("Message Template ID cannot be null or blank");
        }
    }
}