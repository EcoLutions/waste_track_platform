package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Driver;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.DriverLicense;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command.DriverCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories.DriverRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverCommandServiceImpl implements DriverCommandService {
    private final DriverRepository driverRepository;

    @Override
    public Optional<Driver> handle(CreateDriverCommand command) {
        try {
            var districtId = new DistrictId(command.districtId());
            var fullName = new FullName(command.firstName(), command.lastName());
            var documentNumber = new DocumentNumber(command.documentNumber());
            var phoneNumber = new PhoneNumber(command.phoneNumber());
            var userId = new UserId(command.userId());
            var driverLicense = new DriverLicense(command.driverLicense());
            var emailAddress = new EmailAddress(command.emailAddress());

            var driver = new Driver(
                districtId,
                fullName,
                documentNumber,
                phoneNumber,
                userId,
                driverLicense,
                command.licenseExpiryDate(),
                emailAddress
            );
            var savedDriver = driverRepository.save(driver);
            return Optional.of(savedDriver);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create driver: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Driver> handle(UpdateDriverCommand command) {
        try {
            var existingDriver = driverRepository.findById(command.driverId())
                    .orElseThrow(() -> new IllegalArgumentException("Driver with ID " + command.driverId() + " not found."));

            if (!command.districtId().isBlank()) {
                existingDriver.setDistrictId(new DistrictId(command.districtId()));
            }
            if (!command.firstName().isBlank() && !command.lastName().isBlank()) {
                existingDriver.setFullName(new FullName(command.firstName(), command.lastName()));
            }
            if (!command.documentNumber().isBlank()) {
                existingDriver.setDocumentNumber(new DocumentNumber(command.documentNumber()));
            }
            if (!command.phoneNumber().isBlank()) {
                existingDriver.setPhoneNumber(new PhoneNumber(command.phoneNumber()));
            }
            if (!command.userId().isBlank()) {
                existingDriver.setUserId(new UserId(command.userId()));
            }
            if (!command.driverLicense().isBlank()) {
                existingDriver.setDriverLicense(new DriverLicense(command.driverLicense()));
            }
            if (command.licenseExpiryDate() != null) {
                existingDriver.setLicenseExpiryDate(command.licenseExpiryDate());
            }
            if (!command.emailAddress().isBlank()) {
                existingDriver.setEmailAddress(new EmailAddress(command.emailAddress()));
            }

            var savedDriver = driverRepository.save(existingDriver);
            return Optional.of(savedDriver);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update driver: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteDriverCommand command) {
        try {
            var existingDriver = driverRepository.findById(command.driverId())
                    .orElseThrow(() -> new IllegalArgumentException("Driver with ID " + command.driverId() + " not found."));

            driverRepository.delete(existingDriver);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete driver: " + e.getMessage(), e);
        }
    }
}
