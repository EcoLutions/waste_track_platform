package com.ecolutions.platform.wastetrackplatform.communityrelations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Citizen;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.CreateCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UpdateCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.DeleteCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command.CitizenCommandService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories.CitizenRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitizenCommandServiceImpl implements CitizenCommandService {
    private final CitizenRepository citizenRepository;

    @Override
    public Optional<Citizen> handle(CreateCitizenCommand command) {
        try {
            var userId = new UserId(command.userId());
            var districtId = new DistrictId(command.districtId());
            var fullName = new FullName(command.firstName(), command.lastName());
            var email = new EmailAddress(command.email());
            var phoneNumber = new PhoneNumber(command.phoneNumber());

            var citizen = new Citizen(userId, districtId, fullName, email, phoneNumber);

            var savedCitizen = citizenRepository.save(citizen);
            return Optional.of(savedCitizen);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create citizen: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Citizen> handle(UpdateCitizenCommand command) {
        try {
            var existingCitizen = citizenRepository.findById(command.citizenId())
                    .orElseThrow(() -> new IllegalArgumentException("Citizen with ID " + command.citizenId() + " not found."));

            if(command.firstName() != null && command.lastName() != null) {
                var newFullName = new FullName(command.firstName(), command.lastName());
                existingCitizen.setFullName(newFullName);
            }

            if(command.email() != null) {
                var newEmail = new EmailAddress(command.email());
                existingCitizen.setEmail(newEmail);
            }

            if(command.phoneNumber() != null) {
                var newPhoneNumber = new PhoneNumber(command.phoneNumber());
                existingCitizen.setPhoneNumber(newPhoneNumber);
            }

            if(command.districtId() != null) {
                var newDistrictId = new DistrictId(command.districtId());
                existingCitizen.changeDistrict(newDistrictId);
            }

            var updatedCitizen = citizenRepository.save(existingCitizen);
            return Optional.of(updatedCitizen);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update citizen: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteCitizenCommand command) {
        try {
            var existingCitizen = citizenRepository.findById(command.citizenId())
                    .orElseThrow(() -> new IllegalArgumentException("Citizen with ID " + command.citizenId() + " not found."));

            citizenRepository.delete(existingCitizen);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete citizen: " + e.getMessage(), e);
        }
    }
}