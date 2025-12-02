package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand;


import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.CreateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.CreateUserResource;

public class CreateUserCommandFromResourceAssembler {
    public static CreateUserCommand toCommandFromResource(CreateUserResource resource) {
        var roles = RoleListFromStringAssembler.toRoleListFromStringList(resource.roles());
        return new CreateUserCommand(resource.email(), resource.username(), roles, resource.districtId());
    }
}
