package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.events.DriverCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.DriverLicense;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.DriverStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
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
    @AttributeOverride(name = "value", column = @Column(name = "document_number", nullable = false, unique = true))
    private DocumentNumber documentNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone_number", nullable = false, unique = true))
    private PhoneNumber phoneNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false, unique = true))
    private UserId userId;

    @Embedded
    @Column(nullable = false, unique = true)
    private DriverLicense driverLicense;

    @Column(name = "license_expiry_date", nullable = false)
    private LocalDate licenseExpiryDate;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email_address", nullable = false, unique = true))
    private EmailAddress emailAddress;

    private Integer totalHoursWorked;

    private LocalDateTime lastRouteCompletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

    public Driver() {
        super();
        this.status = DriverStatus.AVAILABLE;
    }

    public Driver(CreateDriverCommand command) {
        this();
        this.districtId = new DistrictId(command.districtId());
        this.fullName = new FullName(command.firstName(), command.lastName());
        this.documentNumber = new DocumentNumber(command.documentNumber());
        this.phoneNumber = new PhoneNumber(command.phoneNumber());
        this.userId = new UserId(command.userId());
        this.driverLicense = new DriverLicense(command.driverLicense());
        this.licenseExpiryDate = command.licenseExpiryDate();
        this.emailAddress = new EmailAddress(command.emailAddress());
    }

    public void update(UpdateDriverCommand command) {
        if (command.firstName() != null && !command.firstName().isBlank() &&
            command.lastName() != null && !command.lastName().isBlank()) {
            this.fullName = new FullName(command.firstName(), command.lastName());
        }
        if (command.documentNumber() != null && !command.documentNumber().isBlank()) {
            this.documentNumber = new DocumentNumber(command.documentNumber());
        }
        if (command.phoneNumber() != null && !command.phoneNumber().isBlank()) {
            this.phoneNumber = new PhoneNumber(command.phoneNumber());
        }
        if (command.driverLicense() != null && !command.driverLicense().isBlank()) {
            this.driverLicense = new DriverLicense(command.driverLicense());
        }
        if (command.licenseExpiryDate() != null) {
            this.licenseExpiryDate = command.licenseExpiryDate();
        }
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

    public DriverCreatedEvent publishDriverCreatedEvent() {
        return DriverCreatedEvent.builder()
                .source(this)
                .driverId(this.getId())
                .email(EmailAddress.toStringOrNull(this.emailAddress))
                .firstName(this.fullName != null ? this.fullName.firstName() : null)
                .lastName(this.fullName != null ? this.fullName.lastName() : null)
                .documentNumber(DocumentNumber.toStringOrNull(this.documentNumber))
                .phoneNumber(PhoneNumber.toStringOrNull(this.phoneNumber))
                .districtId(DistrictId.toStringOrNull(this.districtId))
                .build();
    }
}
