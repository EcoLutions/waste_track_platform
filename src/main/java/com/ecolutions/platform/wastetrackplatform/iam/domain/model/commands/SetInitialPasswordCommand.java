package com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands;

public record SetInitialPasswordCommand(String activationToken, String password) {
    public SetInitialPasswordCommand {
        if (activationToken == null || activationToken.isBlank()) {
            throw new IllegalArgumentException("Activation token cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }
}
