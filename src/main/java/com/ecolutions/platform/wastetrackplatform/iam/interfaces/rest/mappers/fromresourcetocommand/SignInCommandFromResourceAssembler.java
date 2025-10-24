package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignInCommand;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(
                resource.email(),
                resource.password());
    }
}
