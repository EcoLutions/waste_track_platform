package com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands;

public record DeleteUserCommand(String userId) {
    public DeleteUserCommand {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
    }
}