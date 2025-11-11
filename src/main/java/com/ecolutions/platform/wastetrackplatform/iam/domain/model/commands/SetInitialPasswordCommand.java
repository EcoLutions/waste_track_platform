package com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands;

public record SetInitialPasswordCommand(
        String token,
        String password
) {
}
