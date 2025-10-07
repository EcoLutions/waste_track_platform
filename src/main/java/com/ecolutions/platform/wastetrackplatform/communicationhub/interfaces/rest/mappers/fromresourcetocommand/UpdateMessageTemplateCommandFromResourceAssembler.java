package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.UpdateMessageTemplateCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.UpdateMessageTemplateResource;

public class UpdateMessageTemplateCommandFromResourceAssembler {
    public static UpdateMessageTemplateCommand toCommandFromResource(UpdateMessageTemplateResource resource) {
        return new UpdateMessageTemplateCommand(
            resource.messageTemplateId(),
            resource.name(),
            resource.category(),
            resource.supportedChannels(),
            resource.emailSubject(),
            resource.emailBody(),
            resource.smsBody(),
            resource.pushTitle(),
            resource.pushBody(),
            resource.variables(),
            resource.isActive()
        );
    }
}