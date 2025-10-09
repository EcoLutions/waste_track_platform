package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.NotificationRequestId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DeliveryAttempt extends AuditableAbstractAggregateRoot<DeliveryAttempt> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "request_id"))
    private NotificationRequestId requestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @NotNull
    private String providerMessageId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AttemptStatus status;

    private Integer attemptNumber;

    @NotNull
    private Boolean canRetry;

    @NotNull
    private LocalDateTime sentAt;

    private LocalDateTime deliveredAt;

    private String errorCode;

    @Column(length = 1000)
    private String errorMessage;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "cost_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "cost_currency"))
    })
    private Money cost;

    public DeliveryAttempt() {
        super();
        this.status = AttemptStatus.PENDING;
        this.canRetry = true;
        this.attemptNumber = 1;
        this.sentAt = LocalDateTime.now();
    }

    public DeliveryAttempt(NotificationRequestId requestId, NotificationChannel channel, ProviderType provider,
                          Integer attemptNumber) {
        this();
        this.requestId = requestId;
        this.channel = channel;
        this.provider = provider;
        this.attemptNumber = attemptNumber;
    }

    public void markAsDelivered(String providerMessageId) {
        if (this.status != AttemptStatus.PENDING) {
            throw new IllegalStateException("Cannot mark as delivered an attempt that is not pending");
        }
        this.status = AttemptStatus.DELIVERED;
        this.providerMessageId = providerMessageId;
        this.deliveredAt = LocalDateTime.now();
        this.canRetry = false;
    }

    public void markAsFailed(String errorCode, String errorMessage, boolean retryable) {
        if (this.status != AttemptStatus.PENDING) {
            throw new IllegalStateException("Cannot mark as failed an attempt that is not pending");
        }
        this.status = AttemptStatus.FAILED;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.canRetry = retryable;
    }

    public void markAsBounced(String errorCode, String errorMessage) {
        if (this.status != AttemptStatus.PENDING) {
            throw new IllegalStateException("Cannot mark as bounced an attempt that is not pending");
        }
        this.status = AttemptStatus.BOUNCED;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.canRetry = false;
    }

    public boolean canBeRetried() {
        return this.canRetry && this.status == AttemptStatus.FAILED;
    }

    public Duration calculateDeliveryTime() {
        if (this.deliveredAt == null) {
            return null;
        }
        return Duration.between(this.sentAt, this.deliveredAt);
    }

    public boolean isSuccessful() {
        return this.status == AttemptStatus.DELIVERED;
    }

    public boolean isFailed() {
        return this.status == AttemptStatus.FAILED || this.status == AttemptStatus.BOUNCED;
    }

    public void incrementAttemptNumber() {
        this.attemptNumber++;
    }

    public void setCost(BigDecimal amount, String currency) {
        this.cost = new Money(currency, amount);
    }
}