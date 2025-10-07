package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record MessageTemplateResource(
    String id,
    String name,
    String category,
    List<String> supportedChannels,
    String emailSubject,
    String emailBody,
    String smsBody,
    String pushTitle,
    String pushBody,
    List<String> variables,
    Boolean isActive,
    String createdAt,
    String updatedAt
) {
}