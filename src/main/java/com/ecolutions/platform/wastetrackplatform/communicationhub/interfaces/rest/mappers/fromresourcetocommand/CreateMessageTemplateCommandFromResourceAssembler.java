package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateMessageTemplateCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.CreateMessageTemplateResource;

public class CreateMessageTemplateCommandFromResourceAssembler {
    public static CreateMessageTemplateCommand toCommandFromResource(CreateMessageTemplateResource resource) {
        return new CreateMessageTemplateCommand(
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