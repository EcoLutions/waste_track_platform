package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.SensorReading;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.CurrentFillLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.SensorReadingCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorReadingCommandServiceImpl implements SensorReadingCommandService {
    private final SensorReadingRepository sensorReadingRepository;
    private final ContainerRepository containerRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Optional<SensorReading> handle(CreateSensorReadingCommand command) {
        // 1. Create sensor reading
        var newSensorReading = new SensorReading(command);
        var savedSensorReading = sensorReadingRepository.save(newSensorReading);

        // 2. Check if container exists
        Container container = containerRepository.findById(command.containerId())
                .orElseThrow(() -> new IllegalArgumentException("Container not found: " + command.containerId()));

        // Save previous fill level before updating
        CurrentFillLevel previousFillLevel = container.getCurrentFillLevel();
        CurrentFillLevel newFillLevel = new CurrentFillLevel(command.fillLevelPercentage());

        // Update container
        container.updateFillLevel(newFillLevel, LocalDateTime.now());
        containerRepository.save(container);

        // 3. Check if the container became critical and publish the event
        if (container.hasBecomeCritical(previousFillLevel)) {
            log.info("Container {} became CRITICAL (fill level: {}%)", container.getId(), newFillLevel.percentage());
            var event = container.buildContainerBecameCriticalEvent();
            eventPublisher.publishEvent(event);
        }

        return Optional.of(savedSensorReading);
    }

    @Override
    public Optional<SensorReading> handle(UpdateSensorReadingCommand command) {
        SensorReading existingSensorReading = sensorReadingRepository.findById(command.sensorReadingId())
                .orElseThrow(() -> new IllegalArgumentException("SensorReading with ID " + command.sensorReadingId() + " not found."));
        existingSensorReading.update(command);
        var updatedSensorReading = sensorReadingRepository.save(existingSensorReading);
        return Optional.of(updatedSensorReading);
    }

    @Override
    public Boolean handle(DeleteSensorReadingCommand command) {
        var existingSensorReading = sensorReadingRepository.findById(command.sensorReadingId())
                .orElseThrow(() -> new IllegalArgumentException("SensorReading with ID " + command.sensorReadingId() + " not found."));
        sensorReadingRepository.delete(existingSensorReading);
        return true;
    }
}