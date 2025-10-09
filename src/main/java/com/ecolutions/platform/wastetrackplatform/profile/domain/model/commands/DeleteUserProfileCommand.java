package com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands;

public record DeleteUserProfileCommand(String userProfileId) {
    public DeleteUserProfileCommand {
        if (userProfileId == null || userProfileId.isBlank()) {
            throw new IllegalArgumentException("User Profile ID cannot be null or blank");
        }
    }
}