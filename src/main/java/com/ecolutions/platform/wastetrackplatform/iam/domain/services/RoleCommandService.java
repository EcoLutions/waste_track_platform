package com.ecolutions.platform.wastetrackplatform.iam.domain.services;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
