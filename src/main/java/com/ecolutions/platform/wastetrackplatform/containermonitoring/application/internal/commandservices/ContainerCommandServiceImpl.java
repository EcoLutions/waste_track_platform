package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.SensorId;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.ContainerCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DeviceId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContainerCommandServiceImpl implements ContainerCommandService {
    private final ContainerRepository containerRepository;

    @Override
    public Optional<Container> handle(CreateContainerCommand command) {
        Optional.ofNullable(command.deviceId()).ifPresent(deviceId -> {
            if (containerRepository.existsByDeviceId(new DeviceId(deviceId))) throw new IllegalArgumentException("Container with device Id " + deviceId + " already exists.");});
        if (containerRepository.existsByLocation(new Location(new BigDecimal(command.latitude()), new BigDecimal(command.longitude()))))
            throw new IllegalArgumentException("A container with location" + command.latitude() + "," + command.longitude() + " already exists.");
        var container = new Container(command);
        var savedContainer = containerRepository.save(container);
        return Optional.of(savedContainer);
    }

    @Override
    public Optional<Container> handle(UpdateContainerCommand command) {
        var existingContainer = containerRepository.findById(command.containerId())
                .orElseThrow(() -> new IllegalArgumentException("Container with ID " + command.containerId() + " not found."));
        if (command.deviceId() != null && !command.deviceId().equals(DeviceId.toStringOrNull(existingContainer.getDeviceId()))) {
            if (containerRepository.existsByDeviceId(new DeviceId(command.deviceId())))
                throw new IllegalArgumentException("A container with device Id" + command.deviceId() + " already exists.");
        }
        if (command.latitude() != null && command.longitude() != null) {
            if (containerRepository.existsByLocation(new Location(new BigDecimal(command.latitude()), new BigDecimal(command.longitude()))))
                throw new IllegalArgumentException("A container with location" + command.latitude() + "," + command.longitude() + " already exists.");
        }
        existingContainer.update(command);
        var updatedContainer = containerRepository.save(existingContainer);
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