package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.email.EmailService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.email.templates.EmailTemplateBuilder;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendPasswordResetEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendUserInvitationEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendWelcomeEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.EmailNotificationCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationCommandCommandServiceImpl implements EmailNotificationCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationCommandCommandServiceImpl.class);

    private final EmailService emailService;
    private final EmailTemplateBuilder templateBuilder;

    public EmailNotificationCommandCommandServiceImpl(
            EmailService emailService,
            EmailTemplateBuilder templateBuilder
    ) {
        this.emailService = emailService;
        this.templateBuilder = templateBuilder;
    }

    @Override
    public void handle(SendUserInvitationEmailCommand command) {
        try {
            String subject = "Welcome to WasteTrack - Your Account Details";
            String htmlContent = templateBuilder.buildUserInvitationTemplate(
                    command.recipientEmail(),
                    command.temporaryPassword(),
                    command.roleName()
            );

            emailService.sendHtmlEmail(
                    command.recipientEmail(),
                    subject,
                    htmlContent
            );
        } catch (Exception e) {
            LOGGER.error("Failed to send user invitation email to: {}", command.recipientEmail(), e);
        }
    }

    @Override
    public void handle(SendPasswordResetEmailCommand command) {
        try {
            String subject = "Reset Your Password - WasteTrack";
            String htmlContent = templateBuilder.buildPasswordResetTemplate(command.resetToken());

            emailService.sendHtmlEmail(
                    command.recipientEmail(),
                    subject,
                    htmlContent
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    @Override
    public void handle(SendWelcomeEmailCommand command) {
        try {
            String subject = "Welcome to WasteTrack Platform!";
            String htmlContent = templateBuilder.buildWelcomeTemplate(command.userName());

            emailService.sendHtmlEmail(
                    command.recipientEmail(),
                    subject,
                    htmlContent
            );

        } catch (Exception e) {
            LOGGER.error("❌ Failed to send welcome email to: {}", command.recipientEmail(), e);
        }
    }
}
