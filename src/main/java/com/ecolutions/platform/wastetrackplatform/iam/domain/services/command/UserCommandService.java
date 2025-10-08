package com.ecolutions.platform.wastetrackplatform.iam.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.CreateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.DeleteUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.UpdateUserCommand;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(CreateUserCommand command);
    Optional<User> handle(UpdateUserCommand command);
    Boolean handle(DeleteUserCommand command);
}