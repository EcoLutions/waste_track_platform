package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.DeviceIdentifier;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.SensorId;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.ContainerCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DeviceId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContainerCommandServiceImpl implements ContainerCommandService {
    private final ContainerRepository containerRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DeviceRepository deviceRepository;

    @Override
    public Optional<Container> handle(CreateContainerCommand command) {
        log.info("handle(CreateContainerCommand) invoked. container location={}, deviceIdentifier='{}'",
                command.latitude() + "," + command.longitude(), command.deviceIdentifier());

        if (containerRepository.existsByLocation(new Location(new BigDecimal(command.latitude()), new BigDecimal(command.longitude())))) {
            throw new IllegalArgumentException("A container with location " + command.latitude() + "," + command.longitude() + " already exists.");
        }

        DeviceId deviceIdObj = null;
        if (command.deviceIdentifier() != null && !command.deviceIdentifier().isBlank()) {
            var deviceOpt = deviceRepository.findByDeviceIdentifier(new DeviceIdentifier(command.deviceIdentifier().trim()));
            log.info("Looking for device with identifier {}: found={}", command.deviceIdentifier(), deviceOpt.isPresent());
            var device = deviceOpt.orElseThrow(() -> new IllegalArgumentException("Device with identifier " + command.deviceIdentifier() + " not found."));
            log.info("Using device with ID {} for container", device.getId());
            deviceIdObj = new DeviceId(device.getId());
            if (containerRepository.existsByDeviceId(deviceIdObj)) {
                throw new IllegalArgumentException("Container with device Id " + deviceIdObj.value() + " already exists.");
            }
        }

        var container = new Container(command, deviceIdObj);
        var savedContainer = containerRepository.save(container);
        return Optional.of(savedContainer);
    }

    @Override
    @Transactional
    public Optional<Container> handle(UpdateContainerCommand command) {
        var existingContainer = containerRepository.findById(command.containerId())
                .orElseThrow(() -> new IllegalArgumentException("Container with ID " + command.containerId() + " not found."));
        if (command.deviceId() != null && !command.deviceId().equals(DeviceId.toStringOrNull(existingContainer.getDeviceId()))) {
            if (containerRepository.existsByDeviceId(new DeviceId(command.deviceId())))
                throw new IllegalArgumentException("A container with device Id" + command.deviceId() + " already exists.");
        }

        if (command.latitude() != null && command.longitude() != null) {
            var newLocation = new Location(new BigDecimal(command.latitude()), new BigDecimal(command.longitude()));
            var containerAtLocation = containerRepository.findByLocation(newLocation);
            if (containerAtLocation.isPresent() && !containerAtLocation.get().getId().equals(existingContainer.getId())) {
                throw new IllegalArgumentException("A container with location " + command.latitude() + "," + command.longitude() + " already exists.");
            }
        }

        var previousMaxFillLevel = existingContainer.getCapacity().maxFillLevel();
        existingContainer.update(command);
        var updatedContainer = containerRepository.save(existingContainer);

        if (!previousMaxFillLevel.equals(updatedContainer.getCapacity().maxFillLevel())) {
            log.info("Container {} max fill level updated to {}", updatedContainer.getId(), updatedContainer.getCapacity().maxFillLevel());
            var event = updatedContainer.buildContainerFillLevelUpdatedEvent();
            eventPublisher.publishEvent(event);
        }

        return Optional.of(updatedContainer);
    }

    @Override
    public Boolean handle(DeleteContainerCommand command) {
        var existingContainer = containerRepository.findById(command.containerId())
                .orElseThrow(() -> new IllegalArgumentException("Container with ID " + command.containerId() + " not found."));
        containerRepository.delete(existingContainer);
        return true;
    }
}