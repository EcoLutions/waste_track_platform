package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class NotificationRequest extends AuditableAbstractAggregateRoot<NotificationRequest> {

    @NotNull
    @Enumerated(EnumType.STRING)
    private SourceContext sourceContext;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "recipient_id"))
    private RecipientId recipientId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RecipientType recipientType;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "recipient_email"))
    private EmailAddress recipientEmail;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "recipient_phone"))
    private PhoneNumber recipientPhone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "template_id"))
    private TemplateId templateId;

    @ElementCollection
    @CollectionTable(name = "notification_request_template_data")
    @MapKeyColumn(name = "data_key")
    @Column(name = "data_value")
    private Map<String, String> templateData;

    @ElementCollection(targetClass = NotificationChannel.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "notification_request_channels")
    private List<NotificationChannel> channels;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationPriority priority;

    @NotNull
    private LocalDateTime scheduledFor;

    @NotNull
    private LocalDateTime expiresAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime sentAt;

    private String failureReason;

    public NotificationRequest() {
        super();
        this.channels = new ArrayList<>();
        this.templateData = Map.of();
        this.status = RequestStatus.PENDING;
    }

    public NotificationRequest(SourceContext sourceContext, RecipientId recipientId, RecipientType recipientType,
                              EmailAddress recipientEmail, PhoneNumber recipientPhone, MessageType messageType,
                              TemplateId templateId, Map<String, String> templateData, List<NotificationChannel> channels,
                              NotificationPriority priority, LocalDateTime scheduledFor, LocalDateTime expiresAt) {
        this();
        this.sourceContext = sourceContext;
        this.recipientId = recipientId;
        this.recipientType = recipientType;
        this.recipientEmail = recipientEmail;
        this.recipientPhone = recipientPhone;
        this.messageType = messageType;
        this.templateId = templateId;
        this.templateData = templateData != null ? templateData : Map.of();
        this.channels = channels != null ? new ArrayList<>(channels) : new ArrayList<>();
        this.priority = priority;
        this.scheduledFor = scheduledFor;
        this.expiresAt = expiresAt;
    }

    public void send() {
        if (this.status != RequestStatus.PENDING) {
            throw new IllegalStateException("Cannot send notification that is not in PENDING status");
        }
        this.status = RequestStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public void fail(String reason) {
        if (this.status == RequestStatus.EXPIRED) {
            throw new IllegalStateException("Cannot fail notification that is already expired");
        }
        this.status = RequestStatus.FAILED;
        this.failureReason = reason;
    }

    public void expire() {
        if (this.status != RequestStatus.PENDING) {
            throw new IllegalStateException("Cannot expire notification that is not in PENDING status");
        }
        this.status = RequestStatus.EXPIRED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean shouldSendNow() {
        return LocalDateTime.now().isAfter(this.scheduledFor) || LocalDateTime.now().isEqual(this.scheduledFor);
    }

    public boolean isPending() {
        return this.status == RequestStatus.PENDING;
    }

    public boolean canRetry() {
        return this.status == RequestStatus.FAILED && !isExpired();
    }

    public void addChannel(NotificationChannel channel) {
        if (this.channels == null) {
            this.channels = new ArrayList<>();
        }
        if (!this.channels.contains(channel)) {
            this.channels.add(channel);
        }
    }

    public void removeChannel(NotificationChannel channel) {
        if (this.channels != null) {
            this.channels.remove(channel);
        }
    }

    public boolean hasChannel(NotificationChannel channel) {
        return this.channels != null && this.channels.contains(channel);
    }
}