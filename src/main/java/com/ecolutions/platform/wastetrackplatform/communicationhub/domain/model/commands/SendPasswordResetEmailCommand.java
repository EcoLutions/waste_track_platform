package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.SourceContext;

public record SendPasswordResetEmailCommand(
        SourceContext sourceContext,
        String recipientEmail,
        String resetToken
) {
}
