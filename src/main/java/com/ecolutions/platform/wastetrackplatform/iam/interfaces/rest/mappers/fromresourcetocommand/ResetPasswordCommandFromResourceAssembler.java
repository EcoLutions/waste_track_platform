package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.ResetPasswordCommand;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.ResetPasswordResource;

public class ResetPasswordCommandFromResourceAssembler {
    public static ResetPasswordCommand toCommandFromResource(ResetPasswordResource resource) {
        return new ResetPasswordCommand(
                resource.resetToken(),
                resource.newPassword()
        );
    }
}
