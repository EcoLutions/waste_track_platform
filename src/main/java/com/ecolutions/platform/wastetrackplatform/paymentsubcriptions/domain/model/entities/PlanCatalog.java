package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.entities;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.entities.AuditableModel;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
    private Money monthlyPrice;

    @NotNull
    private Integer maxVehicles;

    @NotNull
    private Integer maxDrivers;

    @NotNull
    private Integer maxContainers;

    public PlanCatalog() {
        super();
    }

    public PlanCatalog(String name, Money monthlyPrice,
                      Integer maxVehicles, Integer maxDrivers, Integer maxContainers) {
        this();
        this.name = name;
        this.monthlyPrice = monthlyPrice;
        this.maxVehicles = maxVehicles;
        this.maxDrivers = maxDrivers;
        this.maxContainers = maxContainers;
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
