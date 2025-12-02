package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreateSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.SubscriptionStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "district_id", nullable = false))
    private DistrictId districtId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "plan_id", nullable = false))
    private PlanId planId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "monthly_price_amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "monthly_price_currency", nullable = false))
    })
    private Money subscribedPrice;

    @Enumerated(EnumType.STRING)
    private BillingPeriod subscribedBillingPeriod;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private LocalDate trialEndDate;

    @NotNull
    private LocalDate currentPeriodStart;

    @NotNull
    private LocalDate currentPeriodEnd;

    @NotNull
    private LocalDate nextBillingDate;

    private LocalDate gracePeriodEndDate;

    private LocalDate cancelledAt;

    public Subscription() {
        super();
        this.status = SubscriptionStatus.TRIAL;
    }

    public Subscription(CreateSubscriptionCommand command, Money subscribedPrice, BillingPeriod subscribedBillingPeriod) {
        this();
        this.districtId = command.districtId();
        this.planId = command.planId();
        this.startDate = LocalDate.now();
        this.trialEndDate = this.startDate.plusDays(30);
        this.currentPeriodStart = this.startDate;
        this.currentPeriodEnd = this.trialEndDate;
        this.nextBillingDate = this.trialEndDate;
        this.status = SubscriptionStatus.TRIAL;
        this.subscribedPrice = subscribedPrice;
        this.subscribedBillingPeriod = subscribedBillingPeriod;
    }

    public void activate() {
        if (this.status != SubscriptionStatus.TRIAL) {
            throw new IllegalStateException("Can only activate trial subscriptions");
        }
        this.status = SubscriptionStatus.ACTIVE;
        this.currentPeriodStart = LocalDate.now();
        this.currentPeriodEnd = calculateNextPeriodEnd(this.currentPeriodStart);
        this.nextBillingDate = this.currentPeriodEnd;
    }

    public void suspend(String reason) {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Can only suspend active subscriptions");
        }
        this.status = SubscriptionStatus.SUSPENDED;
    }

    public void enterGracePeriod() {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Can only enter grace period for active subscriptions");
        }
        this.status = SubscriptionStatus.SUSPENDED;
        this.gracePeriodEndDate = LocalDate.now().plusDays(3);
    }

    public void cancel(String reason) {
        if (this.status == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Subscription is already cancelled");
        }
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = LocalDate.now();
        this.endDate = LocalDate.now();
    }

    public void renew() {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Can only renew active subscriptions");
        }
        this.currentPeriodStart = this.currentPeriodEnd;
        this.currentPeriodEnd = calculateNextPeriodEnd(this.currentPeriodEnd);
        this.nextBillingDate = this.currentPeriodEnd;
    }

    public void reactivate() {
        if (this.status != SubscriptionStatus.SUSPENDED) {
            throw new IllegalStateException("Can only reactivate SUSPENDED subscriptions. Current status: " + this.status);
        }

        this.status = SubscriptionStatus.ACTIVE;
        this.gracePeriodEndDate = null;

        if (LocalDate.now().isAfter(this.currentPeriodEnd)) {
            this.currentPeriodStart = LocalDate.now();
            this.currentPeriodEnd = calculateNextPeriodEnd(this.currentPeriodStart);
            this.nextBillingDate = this.currentPeriodEnd;
        }
    }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE;
    }

    public boolean isTrial() {
        return this.status == SubscriptionStatus.TRIAL;
    }

    public boolean isSuspended() {
        return this.status == SubscriptionStatus.SUSPENDED;
    }

    public boolean isCancelled() {
        return this.status == SubscriptionStatus.CANCELLED;
    }

    public boolean isInGracePeriod() {
        return this.status == SubscriptionStatus.SUSPENDED
                && this.gracePeriodEndDate != null
                && LocalDate.now().isBefore(this.gracePeriodEndDate);
    }

    public boolean isGracePeriodExpired() {
        return this.gracePeriodEndDate != null
                && LocalDate.now().isAfter(this.gracePeriodEndDate);
    }

    public boolean isTrialExpired() {
        return this.status == SubscriptionStatus.TRIAL
                && LocalDate.now().isAfter(this.trialEndDate);
    }

    public boolean isPeriodExpired() {
        return LocalDate.now().isAfter(this.currentPeriodEnd);
    }

    public int getDaysUntilNextBilling() {
        if (this.status != SubscriptionStatus.ACTIVE) {
            return 0;
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.now(),
                this.nextBillingDate
        );
    }

    public int getDaysInGracePeriod() {
        if (!isInGracePeriod()) {
            return 0;
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.now(),
                this.gracePeriodEndDate
        );
    }

    private LocalDate calculateNextPeriodEnd(LocalDate periodStart) {
        return switch (this.subscribedBillingPeriod) {
            case MONTHLY -> periodStart.plusMonths(1);
            case QUARTERLY -> periodStart.plusMonths(3);
            case SEMI_ANNUAL -> periodStart.plusMonths(6);
            case ANNUAL -> periodStart.plusYears(1);
        };
    }
}
