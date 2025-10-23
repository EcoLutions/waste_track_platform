package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignUpCommand;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SignUpResource;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        var roles = RoleListFromStringAssembler.toRoleListFromStringList(resource.roles());
        System.out.println("roles: " + roles);
        return new SignUpCommand(resource.email(), resource.password(), roles);
    }
}
