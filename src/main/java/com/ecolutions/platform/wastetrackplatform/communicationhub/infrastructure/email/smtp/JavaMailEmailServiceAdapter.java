package com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.email.smtp;

import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.email.EmailService;
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

    @Value("${app.email.from-name:WasteTrack Platform}")
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
            throw new RuntimeException("Failed to send HTML email via SMTP", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error sending email", e);
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
            throw new IllegalArgumentException("Failed to send plain text email via SMTP", e);
        }
    }
}
