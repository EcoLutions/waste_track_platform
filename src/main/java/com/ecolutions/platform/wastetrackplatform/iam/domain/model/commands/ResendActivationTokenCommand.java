package com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands;

public record ResendActivationTokenCommand(String userId) {
    public ResendActivationTokenCommand {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
    }
}
