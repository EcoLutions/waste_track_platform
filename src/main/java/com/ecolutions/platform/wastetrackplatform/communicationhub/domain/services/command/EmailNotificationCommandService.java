package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendPasswordResetEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendUserActivationEmailCommand;

public interface EmailNotificationCommandService {
    void handle(SendUserActivationEmailCommand command);
    void handle(SendPasswordResetEmailCommand command);
}
