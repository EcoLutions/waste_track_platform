package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.CreateUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request.CreateUserProfileResource;

public class CreateUserProfileCommandFromResourceAssembler {
    public static CreateUserProfileCommand toCommandFromResource(CreateUserProfileResource resource) {
        return new CreateUserProfileCommand(
            resource.userId(),
            resource.photoUrl(),
            resource.userType(),
            resource.districtId(),
            resource.email(),
            resource.phoneNumber(),
            resource.language(),
            resource.timezone()
        );
    }
}