package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Driver;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command.DriverCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverCommandServiceImpl implements DriverCommandService {
    private final DriverRepository driverRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Optional<Driver> handle(CreateDriverCommand command) {
        var driver = new Driver(command);
        var savedDriver = driverRepository.save(driver);

        var event = savedDriver.publishDriverCreatedEvent();
        eventPublisher.publishEvent(event);

        return Optional.of(savedDriver);
    }

    @Override
    public Optional<Driver> handle(UpdateDriverCommand command) {
        var existingDriver = driverRepository.findById(command.driverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver with ID " + command.driverId() + " not found."));

        existingDriver.update(command);

        var savedDriver = driverRepository.save(existingDriver);
        return Optional.of(savedDriver);
    }

    @Override
    public Boolean handle(DeleteDriverCommand command) {
        var existingDriver = driverRepository.findById(command.driverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver with ID " + command.driverId() + " not found."));
        driverRepository.delete(existingDriver);
        return true;
    }
}
