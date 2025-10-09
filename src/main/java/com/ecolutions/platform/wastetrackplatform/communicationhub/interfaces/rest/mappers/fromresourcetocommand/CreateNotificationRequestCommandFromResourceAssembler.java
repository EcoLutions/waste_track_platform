package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateNotificationRequestCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.CreateNotificationRequestResource;

public class CreateNotificationRequestCommandFromResourceAssembler {

    public static CreateNotificationRequestCommand toCommandFromResource(CreateNotificationRequestResource resource) {
        return new CreateNotificationRequestCommand(
            resource.sourceContext(),
            resource.recipientId(),
            resource.recipientType(),
            resource.recipientEmail(),
            resource.recipientPhone(),
            resource.messageType(),
            resource.templateId(),
            resource.templateData(),
            resource.channels(),
            resource.priority(),
            resource.scheduledFor(),
            resource.expiresAt()
        );
    }
}