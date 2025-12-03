package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendUserActivationEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.EmailNotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service("NotificationUserCreatedEventHandler")
public class UserCreatedEventHandler {
    private final EmailNotificationCommandService emailNotificationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreatedEventHandler.class);

    public UserCreatedEventHandler(EmailNotificationCommandService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void on(UserCreatedEvent event) {
        LOGGER.info("Handling UserCreatedEvent for user: {} with email: {}", event.getUsername(), event.getEmail());
        var command = new SendUserActivationEmailCommand(event.getEmail(), event.getUsername(), event.getActivationToken());
        emailNotificationService.handle(command);
    }
}
