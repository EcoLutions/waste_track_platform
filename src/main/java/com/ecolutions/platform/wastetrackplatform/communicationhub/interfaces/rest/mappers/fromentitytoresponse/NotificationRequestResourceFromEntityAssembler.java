package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.NotificationRequest;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.RecipientId;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.TemplateId;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.NotificationRequestResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

import java.util.stream.Collectors;

public class NotificationRequestResourceFromEntityAssembler {

    public static NotificationRequestResource toResourceFromEntity(NotificationRequest entity) {
        return NotificationRequestResource.builder()
                .id(entity.getId())
                .sourceContext(entity.getSourceContext() != null ? entity.getSourceContext().name() : null)
                .recipientId(RecipientId.toStringOrNull(entity.getRecipientId()))
                .recipientType(entity.getRecipientType() != null ? entity.getRecipientType().name() : null)
                .recipientEmail(EmailAddress.toStringOrNull(entity.getRecipientEmail()))
                .recipientPhone(PhoneNumber.toStringOrNull(entity.getRecipientPhone()))
                .messageType(entity.getMessageType() != null ? entity.getMessageType().name() : null)
                .templateId(TemplateId.toStringOrNull(entity.getTemplateId()))
                .templateData(entity.getTemplateData() != null ? entity.getTemplateData() : null)
                .channels(entity.getChannels() != null ? entity.getChannels().stream().map(Enum::name).collect(Collectors.toList()) : null)
                .priority(entity.getPriority() != null ? entity.getPriority().name() : null)
                .scheduledFor(DateTimeUtils.localDateTimeToStringOrNull(entity.getScheduledFor()))
                .expiresAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getExpiresAt()))
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .sentAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getSentAt()))
                .failureReason(entity.getFailureReason())
                .build();
    }
}