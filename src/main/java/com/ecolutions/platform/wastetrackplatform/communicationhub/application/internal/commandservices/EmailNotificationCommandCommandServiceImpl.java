package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.email.EmailService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.templates.TemplateService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendPasswordResetEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.SendUserActivationEmailCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.EmailNotificationCommandService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailNotificationCommandCommandServiceImpl implements EmailNotificationCommandService {

    private final EmailService emailService;
    private final TemplateService templateService;
    @Value("${app.frontend.url}")
    private String frontendUrl;
    @Value("${authorization.jwt.activation.expiration.days}")
    private int activationTokenExpirationDays;
    @Value("${authorization.jwt.password-reset.expiration.minutes}")
    private int passwordResetTokenExpirationMinutes;

    public EmailNotificationCommandCommandServiceImpl(EmailService emailService, TemplateService templateService) {
        this.emailService = emailService;
        this.templateService = templateService;
    }

    @Override
    public void handle(SendUserActivationEmailCommand command) {

        try {
            String activationLink = buildActivationLink(command.activationToken());
            String expirationText = activationTokenExpirationDays + " días";

            Map<String, String> variables = Map.of(
                    "username", command.username(),
                    "activationLink", activationLink,
                    "activationToken", command.activationToken(),
                    "expirationText", expirationText
            );

            String htmlContent = templateService.render("iam/user-activation.html", variables);
            String subject = "Activa tu cuenta - Ecolutions";

            emailService.sendHtmlEmail(command.recipientEmail(), subject, htmlContent);

        } catch (Exception _) {
        }
    }

    @Override
    public void handle(SendPasswordResetEmailCommand command) {
        try {
            String resetLink = buildResetLink(command.resetToken());
            String expirationText = passwordResetTokenExpirationMinutes + " minutos";

            Map<String, String> variables = Map.of(
                    "username", command.username(),
                    "resetLink", resetLink,
                    "expirationText", expirationText
            );

            String htmlContent = templateService.render("iam/password-reset.html", variables);
            String subject = "Resetea tu contraseña - Ecolutions";

            emailService.sendHtmlEmail(command.recipientEmail(), subject, htmlContent);

        } catch (Exception _) {
        }
    }

    private String buildActivationLink(String token) {
        return frontendUrl + "/activate-account?token=" + token;
    }

    private String buildResetLink(String token) {
        return frontendUrl + "/reset-password?token=" + token;
    }

}
