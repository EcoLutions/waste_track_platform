package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SetInitialPasswordCommand;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SetInitialPasswordResource;

public class SetInitialPasswordCommandFromResourceAssembler {
    public static SetInitialPasswordCommand toCommandFromResource(SetInitialPasswordResource resource) {
        return new SetInitialPasswordCommand(
                resource.activationToken(),
                resource.password()
        );
    }
}
