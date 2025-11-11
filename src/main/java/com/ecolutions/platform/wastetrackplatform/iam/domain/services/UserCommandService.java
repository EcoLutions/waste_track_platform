package com.ecolutions.platform.wastetrackplatform.iam.domain.services;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.*;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(SignUpCommand command);
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);
    Optional<User> handle(CreateUserCommand command);
    Optional<User> handle(SeedSuperAdminCommand command);
    void handle(SetInitialPasswordCommand command);
    void handle(RequestPasswordResetCommand command);
    void handle(ResetPasswordCommand command);
}
