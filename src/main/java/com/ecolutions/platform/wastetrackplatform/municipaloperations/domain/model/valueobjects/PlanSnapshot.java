package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.BillingPeriod;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PlanId;
import jakarta.persistence.*;
import lombok.Getter;

@Embeddable
@Getter
public class PlanSnapshot {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "plan_id"))
    private PlanId planId;
    private String planName;
    private Integer maxVehicles;
    private Integer maxDrivers;
    private Integer maxContainers;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "plan_price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "plan_price_currency"))
    })
    private Money price;
    private BillingPeriod billingPeriod;
    protected PlanSnapshot() {}
    public static PlanSnapshot from(
            PlanId planId,
            String planName,
            Integer maxVehicles,
            Integer maxDrivers,
            Integer maxContainers,
            Money price,
            BillingPeriod billingPeriod
    ) {
        PlanSnapshot snapshot = new PlanSnapshot();
        snapshot.planId = planId;
        snapshot.planName = planName;
        snapshot.maxVehicles = maxVehicles;
        snapshot.maxDrivers = maxDrivers;
        snapshot.maxContainers = maxContainers;
        snapshot.price = price;
        snapshot.billingPeriod = billingPeriod;
        return snapshot;
    }

    public static String planIdToStringOrNull(PlanSnapshot snapshot) {
        return snapshot != null ? PlanId.toStringOrNull(snapshot.getPlanId()) : null;
    }

    public static String planNameOrNull(PlanSnapshot snapshot) {
        return snapshot != null ? snapshot.getPlanName() : null;
    }

    public static Integer maxVehiclesOrNull(PlanSnapshot snapshot) {
        return snapshot != null ? snapshot.getMaxVehicles() : null;
    }

    public static Integer maxDriversOrNull(PlanSnapshot snapshot) {
        return snapshot != null ? snapshot.getMaxDrivers() : null;
    }

    public static Integer maxContainersOrNull(PlanSnapshot snapshot) {
        return snapshot != null ? snapshot.getMaxContainers() : null;
    }

    public static String priceAsStringOrNull(PlanSnapshot snapshot) {
        return snapshot != null ? Money.amountAsStringOrNull(snapshot.getPrice()) : null;
    }

    public static String currencyOrNull(PlanSnapshot snapshot) {
        return snapshot != null ? Money.currencyOrNull(snapshot.getPrice()) : null;
    }

    public static String billingPeriodOrNull(PlanSnapshot snapshot) {
        return snapshot != null ? BillingPeriod.toStringOrNull(snapshot.getBillingPeriod()) : null;
    }
}
