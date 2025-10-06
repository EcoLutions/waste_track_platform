package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command.DistrictCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictCommandServiceImpl implements DistrictCommandService {
    private final DistrictRepository districtRepository;

    @Override
    public Optional<District> handle(CreateDistrictCommand command) {
        District newDistrict = new District(
            command.name(),
            command.code(),
            command.boundaries(),
            command.primaryAdminEmail()
        );
        try {
            var savedDistrict = districtRepository.save(newDistrict);
            return Optional.of(savedDistrict);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create district: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<District> handle(UpdateDistrictCommand command) {
        District existingDistrict = districtRepository.findById(command.districtId())
            .orElseThrow(() -> new IllegalArgumentException("District with ID " + command.districtId() + " not found."));

        // Update fields
        existingDistrict.setName(command.name());
        existingDistrict.setCode(command.code());
        existingDistrict.setBoundaries(command.boundaries());
        existingDistrict.setPrimaryAdminEmail(command.primaryAdminEmail());

        try {
            var updatedDistrict = districtRepository.save(existingDistrict);
            return Optional.of(updatedDistrict);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update district: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteDistrictCommand command) {
        District existingDistrict = districtRepository.findById(command.districtId())
            .orElseThrow(() -> new IllegalArgumentException("District with ID " + command.districtId() + " not found."));
        try {
            districtRepository.delete(existingDistrict);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete district: " + e.getMessage(), e);
        }
    }
}