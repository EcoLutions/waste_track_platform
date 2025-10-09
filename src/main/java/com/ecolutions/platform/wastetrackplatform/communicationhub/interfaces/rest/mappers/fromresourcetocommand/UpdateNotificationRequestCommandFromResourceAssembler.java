package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.UpdateNotificationRequestCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.UpdateNotificationRequestResource;

public class UpdateNotificationRequestCommandFromResourceAssembler {
    public static UpdateNotificationRequestCommand toCommandFromResource(UpdateNotificationRequestResource resource) {
        return new UpdateNotificationRequestCommand(
            resource.notificationRequestId(),
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

