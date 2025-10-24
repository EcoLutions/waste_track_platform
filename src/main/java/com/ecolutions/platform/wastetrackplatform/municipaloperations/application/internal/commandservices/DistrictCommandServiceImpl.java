package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command.DistrictCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictCommandServiceImpl implements DistrictCommandService {
    private final DistrictRepository districtRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Optional<District> handle(CreateDistrictCommand command) {
        if (districtRepository.existsByCode(command.code()))
            throw new IllegalArgumentException("District with code " + command.code() + " already exists.");

        District newDistrict = new District(command);
        try {
            District savedDistrict = districtRepository.save(newDistrict);

            var event = savedDistrict.publishDistrictCreatedEvent();

            eventPublisher.publishEvent(event);

            return Optional.of(savedDistrict);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create district: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<District> handle(UpdateDistrictCommand command) {
        try {
            District existingDistrict = districtRepository.findById(command.districtId())
                    .orElseThrow(() -> new IllegalArgumentException("District with ID " + command.districtId() + " not found."));

            if (command.name() != null && !command.name().isBlank()) {
                existingDistrict.setName(command.name());
            }
            if (command.code() != null && !command.code().isBlank()) {
                existingDistrict.setCode(command.code());
            }
            if (command.boundaries() != null && !command.boundaries().boundaryPolygon().isBlank()) {
                existingDistrict.setBoundaries(command.boundaries());
            }
            if (command.primaryAdminEmail() != null && !command.primaryAdminEmail().value().isBlank()) {
                existingDistrict.setPrimaryAdminEmail(command.primaryAdminEmail());
            }
            var updatedDistrict = districtRepository.save(existingDistrict);
            return Optional.of(updatedDistrict);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update district: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteDistrictCommand command) {
        try {
            District existingDistrict = districtRepository.findById(command.districtId())
                    .orElseThrow(() -> new IllegalArgumentException("District with ID " + command.districtId() + " not found."));

            districtRepository.delete(existingDistrict);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete district: " + e.getMessage(), e);
        }
    }
}