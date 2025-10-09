package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DriverId;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.*;
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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "assigned_driver_id"))
    private DriverId assignedDriverId;

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

    public void assignDriver(DriverId driverId) {
        this.assignedDriverId = driverId;
    }

    public void unassignDriver() {
        this.assignedDriverId = null;
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
        return isActive && assignedDriverId != null;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
}
