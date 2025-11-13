package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands;

public record InitializeCitizenCommand(
    String userId
) {
    public InitializeCitizenCommand {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
    }
}
