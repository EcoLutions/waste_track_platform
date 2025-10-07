package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateDeliveryAttemptCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.CreateDeliveryAttemptResource;

public class CreateDeliveryAttemptCommandFromResourceAssembler {
    public static CreateDeliveryAttemptCommand toCommandFromResource(CreateDeliveryAttemptResource resource) {
        return new CreateDeliveryAttemptCommand(
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