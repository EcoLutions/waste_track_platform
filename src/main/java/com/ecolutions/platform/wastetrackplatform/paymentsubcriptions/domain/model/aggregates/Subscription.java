package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.SubscriptionStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PaymentMethodId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PlanId;
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

    @NotNull
    private String planName;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "monthly_price_amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "monthly_price_currency", nullable = false))
    })
    private Money monthlyPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @NotNull
    private LocalDate startDate;

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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "default_payment_method_id"))
    private PaymentMethodId defaultPaymentMethodId;

    public Subscription() {
        super();
        this.status = SubscriptionStatus.TRIAL;
    }

    public Subscription(DistrictId districtId, PlanId planId, String planName, Money monthlyPrice,
                       LocalDate startDate, LocalDate trialEndDate, PaymentMethodId defaultPaymentMethodId) {
        this();
        this.districtId = districtId;
        this.planId = planId;
        this.planName = planName;
        this.monthlyPrice = monthlyPrice;
        this.startDate = startDate;
        this.trialEndDate = trialEndDate;
        this.currentPeriodStart = startDate;
        this.currentPeriodEnd = trialEndDate != null ? trialEndDate.plusMonths(1) : startDate.plusMonths(1);
        this.nextBillingDate = this.currentPeriodEnd;
        this.defaultPaymentMethodId = defaultPaymentMethodId;
    }

    public void activate() {
        if (this.status != SubscriptionStatus.TRIAL) {
            throw new IllegalStateException("Can only activate trial subscriptions");
        }
        this.status = SubscriptionStatus.ACTIVE;
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
    }

    public void renew() {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Can only renew active subscriptions");
        }
        this.currentPeriodStart = this.currentPeriodEnd;
        this.currentPeriodEnd = this.currentPeriodEnd.plusMonths(1);
        this.nextBillingDate = this.currentPeriodEnd;
    }

    public boolean isInGracePeriod() {
        return this.gracePeriodEndDate != null && LocalDate.now().isBefore(this.gracePeriodEndDate);
    }

    public boolean shouldBeBilledToday() {
        return LocalDate.now().equals(this.nextBillingDate) && this.status == SubscriptionStatus.ACTIVE;
    }

    public LocalDate calculateNextBillingDate() {
        return this.currentPeriodEnd;
    }
}
