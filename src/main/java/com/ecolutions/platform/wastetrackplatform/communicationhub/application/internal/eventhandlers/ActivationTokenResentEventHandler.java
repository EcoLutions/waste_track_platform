package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendUserActivationEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.EmailNotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.ActivationTokenResentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service("NotificationActivationTokenResentEventHandler")
public class ActivationTokenResentEventHandler {
    private final EmailNotificationCommandService emailNotificationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivationTokenResentEventHandler.class);

    public ActivationTokenResentEventHandler(EmailNotificationCommandService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void on(ActivationTokenResentEvent event) {
        LOGGER.info("Handling ActivationTokenResentEvent for user: {} with email: {}", event.getUsername(), event.getEmail());
        var command = new SendUserActivationEmailCommand(event.getEmail(), event.getUsername(), event.getActivationToken());
        emailNotificationService.handle(command);
    }
}
