package com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands;

public record UpdateUserCommand(
    String userId,
    String username,
    String email,
    String accountStatus
) {
    public UpdateUserCommand {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (accountStatus == null || accountStatus.isBlank()) {
            throw new IllegalArgumentException("Account status cannot be null or blank");
        }
    }
}