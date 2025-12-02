package com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.adapters.email;

import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.email.EmailService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.exceptions.EmailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class JavaMailEmailServiceAdapter implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    public JavaMailEmailServiceAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send HTML email to: " + to, e);
        } catch (Exception e) {
            throw new EmailSendException("Unexpected error sending HTML email to: " + to, e);
        }
    }

    @Override
    public void sendPlainTextEmail(String to, String subject, String textContent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(textContent);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendException("Failed to send plain text email to: " + to, e);
        }
    }
}