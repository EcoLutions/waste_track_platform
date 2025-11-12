package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendUserActivationEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.EmailNotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.ActivationTokenResentEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("NotificationActivationTokenResentEventHandler")
public class ActivationTokenResentEventHandler {
    private final EmailNotificationCommandService emailNotificationService;

    public ActivationTokenResentEventHandler(EmailNotificationCommandService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    @EventListener(ActivationTokenResentEvent.class)
    @Async
    public void on(ActivationTokenResentEvent event) {
        var command = new SendUserActivationEmailCommand(event.getEmail(), event.getUsername(), event.getActivationToken());
        emailNotificationService.handle(command);
    }
}
