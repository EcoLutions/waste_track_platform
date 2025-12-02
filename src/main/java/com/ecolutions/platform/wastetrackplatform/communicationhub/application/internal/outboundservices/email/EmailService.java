package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.email;

public interface EmailService {
    void sendHtmlEmail(String to, String subject, String htmlContent);
    void sendPlainTextEmail(String to, String subject, String textContent);
}
