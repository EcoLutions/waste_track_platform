package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.ContainerCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContainerCommandServiceImpl implements ContainerCommandService {
    private final ContainerRepository containerRepository;

    @Override
    public Optional<Container> handle(CreateContainerCommand command) {
        // TODO: Add validation of not repeat sensor id, same location
        var container = new Container(command);
        var savedContainer = containerRepository.save(container);
        return Optional.of(savedContainer);
    }

    @Override
    public Optional<Container> handle(UpdateContainerCommand command) {
        // TODO: Add validation of not repeat sensor id, same location
        var existingContainer = containerRepository.findById(command.containerId())
                .orElseThrow(() -> new IllegalArgumentException("Container with ID " + command.containerId() + " not found."));
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