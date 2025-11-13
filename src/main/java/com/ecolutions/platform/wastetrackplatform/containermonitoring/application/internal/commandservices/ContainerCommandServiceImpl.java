package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.ContainerCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.*;
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
        try {
            var location = new Location(new BigDecimal(command.latitude()), new BigDecimal(command.longitude()));
            var capacity = new ContainerCapacity(command.volumeLiters(), command.maxWeightKg());
            var containerType = ContainerType.fromString(command.containerType());
            var districtId = DistrictId.of(command.districtId());
            var collectionFrequency = new CollectionFrequency(command.collectionFrequencyDays());
            var sensorId = command.sensorId() != null ? new SensorId(command.sensorId()) : null;

            var container = new Container(location, capacity, containerType, districtId, collectionFrequency, sensorId);

            var savedContainer = containerRepository.save(container);
            return Optional.of(savedContainer);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create container: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Container> handle(UpdateContainerCommand command) {
        try {
            var existingContainer = containerRepository.findById(command.containerId())
                .orElseThrow(() -> new IllegalArgumentException("Container with ID " + command.containerId() + " not found."));

            if (command.latitude() != null && command.longitude() != null) {
                var location = new Location(
                    new BigDecimal(command.latitude()),
                    new BigDecimal(command.longitude()));
                existingContainer.setLocation(location);
            }

            if (command.volumeLiters() != null && command.maxWeightKg() != null) {
                var capacity = new ContainerCapacity(command.volumeLiters(), command.maxWeightKg());
                existingContainer.setCapacity(capacity);
            }

            if (command.containerType() != null) {
                var containerType = ContainerType.fromString(command.containerType());
                existingContainer.setContainerType(containerType);
            }

            if (command.districtId() != null) {
                var districtId = DistrictId.of(command.districtId());
                existingContainer.setDistrictId(districtId);
            }

            if (command.collectionFrequencyDays() != null) {
                var collectionFrequency = new CollectionFrequency(command.collectionFrequencyDays());
                existingContainer.setCollectionFrequency(collectionFrequency);
            }

            var updatedContainer = containerRepository.save(existingContainer);
            return Optional.of(updatedContainer);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update container: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteContainerCommand command) {
        try {
            var existingContainer = containerRepository.findById(command.containerId())
                .orElseThrow(() -> new IllegalArgumentException("Container with ID " + command.containerId() + " not found."));

            containerRepository.delete(existingContainer);
            return true;

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete container: " + e.getMessage(), e);
        }
    }
}