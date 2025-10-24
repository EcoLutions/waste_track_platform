package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.SourceContext;

public record SendUserInvitationEmailCommand(
        SourceContext sourceContext,
        String recipientEmail,
        String temporaryPassword,
        String roleName
) {
}
