package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Driver extends AuditableAbstractAggregateRoot<Driver> {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "district_id"))
    private DistrictId districtId;

    @Embedded
    private FullName fullName;

    @Embedded
    @Column(nullable = false, unique = true)
    private DocumentNumber documentNumber;

    @Embedded
    @Column(nullable = false, unique = true)
    private PhoneNumber phoneNumber;

    @Embedded
    @Column(nullable = false, unique = true)
    private UserId userId;

    @Embedded
    @Column(nullable = false, unique = true)
    private DriverLicense driverLicense;

    @Column(name = "license_expiry_date", nullable = false)
    private LocalDate licenseExpiryDate;

    @Embedded
    @Column(nullable = false, unique = true)
    private EmailAddress emailAddress;

    private Integer totalHoursWorked;

    private LocalDateTime lastRouteCompletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

    @AttributeOverride(name = "value", column = @Column(name = "assigned_vehicle_id"))
    private VehicleId assignedVehicleId;

    public Driver() {
        super();
        this.status = DriverStatus.AVAILABLE;
    }

    public Driver(DistrictId districtId,
                  FullName fullName, DocumentNumber documentNumber,
                  PhoneNumber phoneNumber, UserId userId,
                  DriverLicense driverLicense, LocalDate licenseExpiryDate,
                  EmailAddress emailAddress) {
        this();
        this.districtId = districtId;
        this.fullName = fullName;
        this.documentNumber = documentNumber;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.driverLicense = driverLicense;
        this.licenseExpiryDate = licenseExpiryDate;
        this.emailAddress = emailAddress;
    }

    public void startRoute() {
        if (status != DriverStatus.AVAILABLE) {
            throw new IllegalStateException("Driver is not available to start a route");
        }
        this.status = DriverStatus.ON_ROUTE;
    }

    public void completeRoute(int hoursWorked) {
        if (status != DriverStatus.ON_ROUTE) {
            throw new IllegalStateException("Driver is not on route to complete");
        }
        this.status = DriverStatus.AVAILABLE;
        this.totalHoursWorked += hoursWorked;
        this.lastRouteCompletedAt = LocalDateTime.now();
    }

    public void goOffDuty() {
        if (status == DriverStatus.ON_ROUTE) {
            throw new IllegalStateException("Cannot go off duty while on route");
        }
        this.status = DriverStatus.OFF_DUTY;
    }

    public void suspend(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Suspension reason cannot be null or blank");
        }
        this.status = DriverStatus.SUSPENDED;
    }

    public void assignVehicle(VehicleId vehicleId) {
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
        this.assignedVehicleId = vehicleId;
    }

    public void unassignVehicle() {
        this.assignedVehicleId = null;
    }

    public void isAvailableForNewRoute() {
        if (this.status != DriverStatus.AVAILABLE) {
            throw new IllegalStateException("Driver is not available for a new route");
        }
        if (isLicenseExpired()) {
            throw new IllegalStateException("Driver's license is expired");
        }
    }

    public void reactivate() {
        this.status = DriverStatus.AVAILABLE;
    }

    public boolean isLicenseExpired() {
        return LocalDate.now().isAfter(licenseExpiryDate);
    }
}
