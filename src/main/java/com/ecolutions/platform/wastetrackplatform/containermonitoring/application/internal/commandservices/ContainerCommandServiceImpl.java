package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.SensorId;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.ContainerCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
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
        if (containerRepository.existsBySensorId(new SensorId(command.sensorId())))
            throw new IllegalArgumentException("A container with sensor Id" + command.sensorId() + " already exists.");
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
        if (command.sensorId() != null && !command.sensorId().equals(SensorId.toStringOrNull(existingContainer.getSensorId()))) {
            if (containerRepository.existsBySensorId(new SensorId(command.sensorId())))
                throw new IllegalArgumentException("A container with sensor Id" + command.sensorId() + " already exists.");
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