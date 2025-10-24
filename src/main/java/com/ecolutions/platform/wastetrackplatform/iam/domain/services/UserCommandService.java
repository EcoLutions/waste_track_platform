package com.ecolutions.platform.wastetrackplatform.iam.domain.services;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.CreateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SeedSuperAdminCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignInCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignUpCommand;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(SignUpCommand command);
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);
    Optional<User> handle(CreateUserCommand command);
    Optional<User> handle(SeedSuperAdminCommand command);
}
