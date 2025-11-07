package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.events.DistrictCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationalStatus operationalStatus;

    private LocalDate serviceStartDate;

    @Embedded
    private PlanSnapshot planSnapshot;

    private Integer currentVehicleCount;
    private Integer currentDriverCount;
    private Integer currentContainerCount;

    public District() {
        super();
        this.operationalStatus = OperationalStatus.ACTIVE;
        this.currentVehicleCount = 0;
        this.currentDriverCount = 0;
        this.currentContainerCount = 0;
    }

    public District(CreateDistrictCommand command) {
        this();
        this.name = command.name();
        this.code = command.code();
    }

    public void update(UpdateDistrictCommand command) {
        if (command.name() != null && !command.name().isBlank() && !command.name().equals(this.name)) {
            this.name = command.name();
        }
        if (command.code() != null && !command.code().isBlank() && !command.code().equals(this.code)) {
            this.code = command.code();
        }
    }

    public void updatePlanSnapshot(PlanSnapshot planSnapshot) {
        if (planSnapshot == null) {
            throw new IllegalArgumentException("Plan snapshot cannot be null");
        }
        this.planSnapshot = planSnapshot;
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

    public boolean canRegisterNewVehicle() {
        if (operationalStatus != OperationalStatus.ACTIVE) return false;
        if (planSnapshot == null) return false;
        return planSnapshot.getMaxVehicles() > currentVehicleCount;
    }

    public boolean canRegisterNewDriver() {
        if (operationalStatus != OperationalStatus.ACTIVE) return false;
        if (planSnapshot == null) return false;
        return planSnapshot.getMaxDrivers() > currentDriverCount;
    }

    public DistrictCreatedEvent publishDistrictCreatedEvent(
            String primaryAdminEmail,
            String primaryAdminUsername,
            String planId
    ) {
        return DistrictCreatedEvent.builder()
                .source(this)
                .districtId(this.getId())
                .name(this.name)
                .code(this.code)
                .primaryAdminEmail(primaryAdminEmail)
                .primaryAdminUsername(primaryAdminUsername)
                .planId(planId)
                .build();
    }
}
