package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.MessageTemplate;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.MessageTemplateResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

import java.util.stream.Collectors;

public class MessageTemplateResourceFromEntityAssembler {
    public static MessageTemplateResource toResourceFromEntity(MessageTemplate entity) {
        return MessageTemplateResource.builder()
            .id(entity.getId())
            .name(entity.getName())
            .category(entity.getCategory().name())
            .supportedChannels(entity.getSupportedChannels() != null ?
                entity.getSupportedChannels().stream().map(Enum::name).collect(Collectors.toList()) : null)
            .emailSubject(entity.getEmailSubject())
            .emailBody(entity.getEmailBody())
            .smsBody(entity.getSmsBody())
            .pushTitle(entity.getPushTitle())
            .pushBody(entity.getPushBody())
            .variables(entity.getVariables())
            .isActive(entity.getIsActive())
            .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}