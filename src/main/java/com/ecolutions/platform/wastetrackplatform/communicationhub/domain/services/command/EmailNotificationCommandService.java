package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendPasswordResetEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendUserInvitationEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendWelcomeEmailCommand;

public interface EmailNotificationCommandService {
    void handle(SendUserInvitationEmailCommand command);
    void handle(SendPasswordResetEmailCommand command);
    void handle(SendWelcomeEmailCommand command);
}
