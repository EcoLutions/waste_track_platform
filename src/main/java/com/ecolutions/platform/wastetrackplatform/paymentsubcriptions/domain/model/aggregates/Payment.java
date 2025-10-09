package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.PaymentStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PaymentMethodId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.SubscriptionId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @NotNull
    @Embedded
    private SubscriptionId subscriptionId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "amount_amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "amount_currency", nullable = false))
    })
    private Money amount;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "payment_method_id", nullable = false))
    private PaymentMethodId paymentMethodId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String gatewayTransactionId;

    private String gatewayResponse;

    @NotNull
    private LocalDateTime scheduledAt;

    private LocalDateTime processedAt;

    private LocalDateTime succeededAt;

    @NotNull
    private Integer attemptNumber;

    private String failureReason;

    @NotNull
    private Boolean canRetry;

    public Payment() {
        super();
        this.attemptNumber = 1;
        this.canRetry = true;
        this.status = PaymentStatus.PENDING;
    }

    public Payment(SubscriptionId subscription, Money amount, PaymentMethodId paymentMethodId, LocalDateTime scheduledAt) {
        this();
        this.subscriptionId = subscription;
        this.amount = amount;
        this.paymentMethodId = paymentMethodId;
        this.scheduledAt = scheduledAt;
    }

    public void markAsProcessing() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Can only mark pending payments as processing");
        }
        this.status = PaymentStatus.PROCESSING;
        this.processedAt = LocalDateTime.now();
    }

    public void markAsSucceeded(String transactionId) {
        if (this.status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Can only mark processing payments as succeeded");
        }
        this.status = PaymentStatus.SUCCEEDED;
        this.gatewayTransactionId = transactionId;
        this.succeededAt = LocalDateTime.now();
        this.canRetry = false;
    }

    public void markAsFailed(String reason, boolean retryable) {
        if (this.status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Can only mark processing payments as failed");
        }
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.canRetry = retryable;
    }

    public boolean canBeRetried() {
        return this.canRetry && this.attemptNumber < 3 && this.status == PaymentStatus.FAILED;
    }

    public LocalDateTime shouldRetryAt() {
        if (!canBeRetried()) {
            throw new IllegalStateException("Payment cannot be retried");
        }
        // Exponential backoff: 1 hour, 4 hours, 24 hours
        int hours = switch (this.attemptNumber) {
            case 1 -> 1;
            case 2 -> 4;
            default -> 24;
        };
        return this.processedAt != null ? this.processedAt.plusHours(hours) : LocalDateTime.now().plusHours(hours);
    }

    public void incrementAttempt() {
        if (this.status != PaymentStatus.FAILED) {
            throw new IllegalStateException("Can only increment attempts for failed payments");
        }
        this.attemptNumber++;
    }
}
