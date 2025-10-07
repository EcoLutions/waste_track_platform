package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.UpdateDeliveryAttemptCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.UpdateDeliveryAttemptResource;

public class UpdateDeliveryAttemptCommandFromResourceAssembler {
    public static UpdateDeliveryAttemptCommand toCommandFromResource(UpdateDeliveryAttemptResource resource) {
        return new UpdateDeliveryAttemptCommand(
            resource.deliveryAttemptId(),
            resource.requestId(),
            resource.channel(),
            resource.provider(),
            resource.providerMessageId(),
            resource.status(),
            resource.attemptNumber(),
            resource.canRetry(),
            resource.sentAt(),
            resource.deliveredAt(),
            resource.errorCode(),
            resource.errorMessage(),
            resource.costAmount(),
            resource.costCurrency()
        );
    }
}