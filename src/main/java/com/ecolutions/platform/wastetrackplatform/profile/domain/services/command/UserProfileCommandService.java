package com.ecolutions.platform.wastetrackplatform.profile.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates.UserProfile;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.CreateUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.DeleteUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.InitializeUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.UpdateUserProfileCommand;

import java.util.Optional;

public interface UserProfileCommandService {
    Optional<UserProfile> handle(CreateUserProfileCommand command);
    Optional<UserProfile> handle(UpdateUserProfileCommand command);
    Boolean handle(DeleteUserProfileCommand command);
    Optional<UserProfile> handle(InitializeUserProfileCommand command);
}