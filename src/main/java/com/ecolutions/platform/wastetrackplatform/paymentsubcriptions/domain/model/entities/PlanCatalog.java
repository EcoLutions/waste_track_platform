package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.entities;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreatePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.BillingPeriod;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.entities.AuditableModel;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Currency;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class PlanCatalog extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    private String name;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "monthly_price_amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "monthly_price_currency", nullable = false))
    })
    private Money price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BillingPeriod billingPeriod;

    @NotNull
    private Integer maxVehicles;

    @NotNull
    private Integer maxDrivers;

    @NotNull
    private Integer maxContainers;

    public PlanCatalog() {
        super();
    }

    public PlanCatalog(CreatePlanCatalogCommand command) {
        this();
        this.name = command.name();
        this.price = new Money(Currency.PEN.name(), new BigDecimal(command.priceAmount()));
        this.billingPeriod = BillingPeriod.fromString(command.billingPeriod());
        this.maxVehicles = command.maxVehicles();
        this.maxDrivers = command.maxDrivers();
        this.maxContainers = command.maxContainers();
    }

    public boolean canAddVehicle(int currentVehicles) {
        return currentVehicles < this.maxVehicles;
    }

    public boolean canAddDriver(int currentDrivers) {
        return currentDrivers < this.maxDrivers;
    }

    public boolean canAddContainer(int currentContainers) {
        return currentContainers < this.maxContainers;
    }

    public int getRemainingVehicles(int currentVehicles) {
        return Math.max(0, this.maxVehicles - currentVehicles);
    }

    public int getRemainingDrivers(int currentDrivers) {
        return Math.max(0, this.maxDrivers - currentDrivers);
    }

    public int getRemainingContainers(int currentContainers) {
        return Math.max(0, this.maxContainers - currentContainers);
    }
}
