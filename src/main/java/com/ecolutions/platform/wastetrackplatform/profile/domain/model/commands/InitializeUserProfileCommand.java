package com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands;

public record InitializeUserProfileCommand(
        String userId,
        String email,
        String districtId
) {
}
