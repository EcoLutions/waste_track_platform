package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.outboundservices.websocket.ContainerWebSocketPublisherService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.outboundservices.websocket.transform.ContainerUpdatedFillLevelPayloadAssembler;
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
    private final ContainerWebSocketPublisherService containerWebSocketPublisher;

    @Override
    @Transactional
    public Optional<SensorReading> handle(CreateSensorReadingCommand command) {
        log.debug("1. Creating sensor reading for container {}", command.containerId());
        var newSensorReading = new SensorReading(command);
        var savedSensorReading = sensorReadingRepository.save(newSensorReading);

        log.debug("2. Updating container fill level for container {}", command.containerId());
        Container container = containerRepository.findById(command.containerId())
                .orElseThrow(() -> new IllegalArgumentException("Container not found: " + command.containerId()));

        log.debug("3. Checking previous fill level for container {}", command.containerId());
        CurrentFillLevel previousFillLevel = container.getCurrentFillLevel();
        CurrentFillLevel newFillLevel = new CurrentFillLevel(command.fillLevelPercentage());

        log.debug("4. Updating container fill level for container {}", command.containerId());
        container.updateFillLevel(newFillLevel, LocalDateTime.now());
        Container updatedContainer = containerRepository.save(container);

        log.debug("5. Publishing events for container {}", command.containerId());
        if (container.hasBecomeCritical(previousFillLevel)) {
            log.info("Container {} became CRITICAL (fill level: {}%)", container.getId(), newFillLevel.percentage());
            var event = container.buildContainerBecameCriticalEvent();
            eventPublisher.publishEvent(event);
        }

        log.debug("6. Publishing websocket events for container {}", command.containerId());
        var containerUpdatedFillLevel = ContainerUpdatedFillLevelPayloadAssembler.toPayload(updatedContainer);
        containerWebSocketPublisher.publishContainerUpdatedFillLevel(containerUpdatedFillLevel);

        log.debug("7. Sensor reading created successfully for container {}", command.containerId());
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