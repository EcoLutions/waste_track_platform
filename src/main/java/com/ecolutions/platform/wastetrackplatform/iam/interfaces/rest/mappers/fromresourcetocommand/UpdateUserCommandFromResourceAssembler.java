package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.UpdateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.UpdateUserResource;

public class UpdateUserCommandFromResourceAssembler {
    public static UpdateUserCommand toCommandFromResource(UpdateUserResource resource, String userId) {
        return new UpdateUserCommand(
            userId,
            resource.username(),
            resource.email(),
            resource.accountStatus()
        );
    }
}
