package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries;

public record GetMessageTemplateByIdQuery(String messageTemplateId) {
    public GetMessageTemplateByIdQuery {
        if (messageTemplateId == null || messageTemplateId.isBlank()) {
            throw new IllegalArgumentException("Message Template ID cannot be null or blank");
        }
    }
}