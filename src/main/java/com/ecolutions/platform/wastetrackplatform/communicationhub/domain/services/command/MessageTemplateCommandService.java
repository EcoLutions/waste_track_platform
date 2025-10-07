package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.MessageTemplate;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateMessageTemplateCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.UpdateMessageTemplateCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.DeleteMessageTemplateCommand;

import java.util.Optional;

public interface MessageTemplateCommandService {
    Optional<MessageTemplate> handle(CreateMessageTemplateCommand command);
    Optional<MessageTemplate> handle(UpdateMessageTemplateCommand command);
    Boolean handle(DeleteMessageTemplateCommand command);
}