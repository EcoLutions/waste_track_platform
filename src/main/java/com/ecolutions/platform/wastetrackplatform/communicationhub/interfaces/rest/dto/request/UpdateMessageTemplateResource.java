package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateMessageTemplateResource(
    @NotBlank String messageTemplateId,
    @NotBlank String name,
    @NotBlank String category,
    @NotEmpty List<String> supportedChannels,
    String emailSubject,
    String emailBody,
    String smsBody,
    String pushTitle,
    String pushBody,
    List<String> variables,
    @NotNull Boolean isActive
) {
    public UpdateMessageTemplateResource {
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