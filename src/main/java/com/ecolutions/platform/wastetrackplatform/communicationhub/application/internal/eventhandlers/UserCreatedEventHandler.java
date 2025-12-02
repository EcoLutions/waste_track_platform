package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendUserActivationEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.EmailNotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.UserCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("NotificationUserCreatedEventHandler")
public class UserCreatedEventHandler {
    private final EmailNotificationCommandService emailNotificationService;

    public UserCreatedEventHandler(EmailNotificationCommandService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }


    @EventListener(UserCreatedEvent.class)
    @Async
    public void on(UserCreatedEvent event) {
        var command = new SendUserActivationEmailCommand(event.getEmail(), event.getUsername(), event.getActivationToken());
        emailNotificationService.handle(command);
    }
}
