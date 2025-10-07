package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.DeliveryAttempt;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.DeliveryAttemptResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.NotificationRequestId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class DeliveryAttemptResourceFromEntityAssembler {
    public static DeliveryAttemptResource toResourceFromEntity(DeliveryAttempt entity) {
        return DeliveryAttemptResource.builder()
            .id(entity.getId())
            .requestId(NotificationRequestId.toStringOrNull(entity.getRequestId()))
            .channel(entity.getChannel().name())
            .provider(entity.getProvider().name())
            .providerMessageId(entity.getProviderMessageId())
            .status(entity.getStatus().name())
            .attemptNumber(entity.getAttemptNumber())
            .canRetry(entity.getCanRetry())
            .sentAt(entity.getSentAt())
            .deliveredAt(entity.getDeliveredAt())
            .errorCode(entity.getErrorCode())
            .errorMessage(entity.getErrorMessage())
            .costAmount(entity.getCost() != null ? entity.getCost().amount().toString() : null)
            .costCurrency(entity.getCost() != null ? entity.getCost().currency() : null)
            .build();
    }
}