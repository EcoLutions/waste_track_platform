package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendPasswordResetEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.EmailNotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.PasswordResetRequestedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("NotificationPasswordResetRequestedEventHandler")
public class PasswordResetRequestedEventHandler {
    private final EmailNotificationCommandService emailNotificationService;

    public PasswordResetRequestedEventHandler(EmailNotificationCommandService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    @EventListener(PasswordResetRequestedEvent.class)
    @Async
    public void on(PasswordResetRequestedEvent event) {
        var command = new SendPasswordResetEmailCommand(event.getEmail(), event.getUsername(), event.getResetToken());
        emailNotificationService.handle(command);
    }
}
