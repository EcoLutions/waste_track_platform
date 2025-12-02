package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {
    @Embedded
    @Column(nullable = false, unique = true)
    @AttributeOverride(name = "value", column = @Column(name = "license_plate"))
    private LicensePlate licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    @Embedded
    @AttributeOverride(name = "cubicMeters", column = @Column(name = "volume_capacity"))
    private VolumeCapacity volumeCapacity;

    @Embedded
    @AttributeOverride(name = "kilograms", column = @Column(name = "weight_capacity"))
    private WeightCapacity weightCapacity;

    @Embedded
    @AttributeOverride(name = "kilometers", column = @Column(name = "mileage"))
    private Mileage mileage;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "district_id"))
    private DistrictId districtId;

    private LocalDateTime lastMaintenanceDate;

    private LocalDateTime nextMaintenanceDate;

    @Column(nullable = false)
    private Boolean isActive;

    public Vehicle() {
        super();
        this.isActive = true;
    }

    public Vehicle(LicensePlate licensePlate, VehicleType vehicleType,
                   VolumeCapacity volumeCapacity, WeightCapacity weightCapacity,
                   DistrictId districtId) {
        this();
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.volumeCapacity = volumeCapacity;
        this.weightCapacity = weightCapacity;
        this.districtId = districtId;
        this.mileage = new Mileage(0);
    }

    public void updateMileage(Mileage newMileage) {
        this.mileage = newMileage;
    }

    public void scheduleMaintenance(LocalDateTime maintenanceDate) {
        this.nextMaintenanceDate = maintenanceDate;
    }

    public void recordMaintenance(LocalDateTime maintenanceDate) {
        this.lastMaintenanceDate = maintenanceDate;
        this.nextMaintenanceDate = null;
    }

    public boolean needsMaintenance() {
        if (nextMaintenanceDate == null) {
            return mileage.needsMaintenance();
        }
        return LocalDateTime.now().isAfter(nextMaintenanceDate) || mileage.needsMaintenance();
    }

    public boolean isAvailable() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
}
