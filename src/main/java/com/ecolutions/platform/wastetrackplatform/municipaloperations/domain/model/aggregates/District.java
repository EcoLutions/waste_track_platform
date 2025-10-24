package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PlanId;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class District extends AuditableAbstractAggregateRoot<District> {
    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
    private String code;

    @Embedded
    private GeographicBoundaries boundaries;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationalStatus operationalStatus;

    private LocalDate serviceStartDate;

    @Transient
    private PlanId currentPlanId;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "subscriptionId.value", column = @Column(name = "subscription_id"))
    )
    private SubscriptionSnapshot subscriptionSnapshot;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "primary_admin_email"))
    private EmailAddress primaryAdminEmail;

    public District() {
        super();
        this.operationalStatus = OperationalStatus.ACTIVE;
    }

    public District(String name, String code, GeographicBoundaries boundaries,
                   EmailAddress primaryAdminEmail) {
        this();
        this.name = name;
        this.code = code;
        this.boundaries = boundaries;
        this.primaryAdminEmail = primaryAdminEmail;
    }

    public District(CreateDistrictCommand command) {
        this();
        this.name = command.name();
        this.code = command.code();
        this.boundaries = command.boundaries();
        this.primaryAdminEmail = command.primaryAdminEmail();
        this.currentPlanId = PlanId.of(command.planId());
    }

    public void activate() {
        this.operationalStatus = OperationalStatus.ACTIVE;
    }

    public void suspend(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Suspension reason cannot be null or blank");
        }
        this.operationalStatus = OperationalStatus.SUSPENDED;
    }

    public boolean isWithinServiceLimits(int vehicleCount, int driverCount) {
        return vehicleCount <= subscriptionSnapshot.maxVehicles() && driverCount <= subscriptionSnapshot.maxDrivers();
    }

    public boolean canRegisterNewVehicle() {
        // TODO: Implement actual limit checking
        // This would need to be checked against actual counts from repository
        // For now, just return true as this is domain logic
        return operationalStatus == OperationalStatus.ACTIVE;
    }

    public boolean canRegisterNewDriver() {
        // TODO: Implement actual limit checking
        // This would need to be checked against actual counts from repository
        // For now, just return true as this is domain logic
        return operationalStatus == OperationalStatus.ACTIVE;
    }

    public boolean isLocationWithinBoundaries(Location location) {
        // TODO: Implement actual geographic boundary checking
        // This would require parsing the boundaryPolygon and checking if location is within
        // For now, return true as placeholder
        return true;
    }
}
