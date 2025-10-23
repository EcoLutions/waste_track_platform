package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendUserInvitationEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.SourceContext;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.EmailNotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.UserCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedEventHandler {
    private final EmailNotificationCommandService emailNotificationCommandService;

    public UserCreatedEventHandler(EmailNotificationCommandService emailNotificationCommandService) {
        this.emailNotificationCommandService = emailNotificationCommandService;
    }

    @EventListener(UserCreatedEvent.class)
    @Async
    public void on(UserCreatedEvent event) {
        var command = new SendUserInvitationEmailCommand(
                SourceContext.IAM,
                event.getEmail(),
                event.getTemporalPassword(),
                event.getRoles().getFirst()
        );

        emailNotificationCommandService.handle(command);
    }
}
