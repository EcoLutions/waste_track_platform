package com.ecolutions.platform.wastetrackplatform.profile.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.UserCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.InitializeUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.services.command.UserProfileCommandService;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Roles;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("ProfileUserCreatedEventHandler")
public class UserCreatedEventHandler {
    private final UserProfileCommandService userProfileCommandService;

    public UserCreatedEventHandler(UserProfileCommandService userProfileCommandService) {
        this.userProfileCommandService = userProfileCommandService;
    }

    @EventListener(UserCreatedEvent.class)
    @Async
    public void on(UserCreatedEvent event) {
        var roles = event.getRoles().stream().map(Roles::fromString).toList();

        var isSystemAdministrator = roles.contains(Roles.ROLE_SYSTEM_ADMINISTRATOR);
        if (isSystemAdministrator) {return;}

        var command = new InitializeUserProfileCommand(
                event.getUserId(),
                event.getEmail(),
                event.getDistrictId()
        );

        userProfileCommandService.handle(command);
    }
}
