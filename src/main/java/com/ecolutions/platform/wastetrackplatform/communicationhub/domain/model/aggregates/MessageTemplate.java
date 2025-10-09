package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationChannel;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.TemplateCategory;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Entity
@Getter
@Setter
public class MessageTemplate extends AuditableAbstractAggregateRoot<MessageTemplate> {

    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TemplateCategory category;

    @ElementCollection(targetClass = NotificationChannel.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "message_template_supported_channels")
    private List<NotificationChannel> supportedChannels;

    @Column(length = 500)
    private String emailSubject;

    @Column(length = 2000)
    private String emailBody;

    @Column(length = 1000)
    private String smsBody;

    @Column(length = 200)
    private String pushTitle;

    @Column(length = 500)
    private String pushBody;

    @ElementCollection
    @CollectionTable(name = "message_template_variables")
    @Column(name = "variable")
    private List<String> variables;

    @Column(nullable = false)
    private Boolean isActive;

    public MessageTemplate() {
        super();
        this.supportedChannels = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.isActive = true;
    }

    public MessageTemplate(String name, TemplateCategory category, List<NotificationChannel> supportedChannels) {
        this();
        this.name = name;
        this.category = category;
        this.supportedChannels = supportedChannels != null ? new ArrayList<>(supportedChannels) : new ArrayList<>();
    }

    public String renderForChannel(NotificationChannel channel, Map<String, String> data) {
        if (!supportsChannel(channel)) {
            throw new IllegalArgumentException("Template does not support channel: " + channel);
        }

        if (data == null) {
            data = Map.of();
        }

        return switch (channel) {
            case EMAIL -> renderEmail(data);
            case SMS -> renderSMS(data);
            case PUSH -> renderPush(data);
        };
    }

    private String renderEmail(Map<String, String> data) {
        String subject = this.emailSubject != null ? substituteVariables(this.emailSubject, data) : "";
        String body = this.emailBody != null ? substituteVariables(this.emailBody, data) : "";
        return "Subject: " + subject + "\n\n" + body;
    }

    private String renderSMS(Map<String, String> data) {
        return this.smsBody != null ? substituteVariables(this.smsBody, data) : "";
    }

    private String renderPush(Map<String, String> data) {
        String title = this.pushTitle != null ? substituteVariables(this.pushTitle, data) : "";
        String body = this.pushBody != null ? substituteVariables(this.pushBody, data) : "";
        return title + "|" + body;
    }

    private String substituteVariables(String template, Map<String, String> data) {
        if (template == null || template.isEmpty()) {
            return template;
        }

        String result = template;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String placeholder = "\\{\\{" + Pattern.quote(entry.getKey()) + "\\}\\}";
            result = result.replaceAll(placeholder, entry.getValue() != null ? entry.getValue() : "");
        }
        return result;
    }

    public boolean supportsChannel(NotificationChannel channel) {
        return this.supportedChannels != null && this.supportedChannels.contains(channel);
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void addSupportedChannel(NotificationChannel channel) {
        if (this.supportedChannels == null) {
            this.supportedChannels = new ArrayList<>();
        }
        if (!this.supportedChannels.contains(channel)) {
            this.supportedChannels.add(channel);
        }
    }

    public void removeSupportedChannel(NotificationChannel channel) {
        if (this.supportedChannels != null) {
            this.supportedChannels.remove(channel);
        }
    }

    public void addVariable(String variable) {
        if (this.variables == null) {
            this.variables = new ArrayList<>();
        }
        if (!this.variables.contains(variable)) {
            this.variables.add(variable);
        }
    }

    public void removeVariable(String variable) {
        if (this.variables != null) {
            this.variables.remove(variable);
        }
    }

    public void updateContent(String emailSubject, String emailBody, String smsBody, String pushTitle, String pushBody) {
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        this.smsBody = smsBody;
        this.pushTitle = pushTitle;
        this.pushBody = pushBody;
    }
}