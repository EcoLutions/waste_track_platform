package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.UpdateUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request.UpdateUserProfileResource;

public class UpdateUserProfileCommandFromResourceAssembler {
    public static UpdateUserProfileCommand toCommandFromResource(String userProfileId, UpdateUserProfileResource resource) {
        return new UpdateUserProfileCommand(
            userProfileId,
            resource.photoUrl(),
            resource.userType(),
            resource.districtId(),
            resource.email(),
            resource.phoneNumber(),
            resource.emailNotificationsEnabled(),
            resource.smsNotificationsEnabled(),
            resource.pushNotificationsEnabled(),
            resource.language(),
            resource.timezone(),
            resource.isActive()
        );
    }
}