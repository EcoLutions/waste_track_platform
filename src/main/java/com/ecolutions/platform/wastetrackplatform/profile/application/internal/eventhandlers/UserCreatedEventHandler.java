package com.ecolutions.platform.wastetrackplatform.profile.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.UserCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.InitializeUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.services.command.UserProfileCommandService;
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
        if(!event.getHasProfile()) return;

        var command = new InitializeUserProfileCommand(event.getUserId(), event.getEmail(), event.getDistrictId());

        userProfileCommandService.handle(command);
    }
}
