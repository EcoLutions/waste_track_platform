package com.ecolutions.platform.wastetrackplatform.communityrelations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Citizen;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.CreateCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.DeleteCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.InitializeCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UpdateCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command.CitizenCommandService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories.CitizenRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitizenCommandServiceImpl implements CitizenCommandService {
    private final CitizenRepository citizenRepository;

    @Override
    public Optional<Citizen> handle(CreateCitizenCommand command) {
        var citizen = new Citizen(command);
        var savedCitizen = citizenRepository.save(citizen);
        return Optional.of(savedCitizen);
    }

    @Override
    public Optional<Citizen> handle(InitializeCitizenCommand command) {
        if (citizenRepository.existsByUserId(new UserId(command.userId())))
            throw new IllegalArgumentException("Citizen already exists for user ID: " + command.userId());

        var citizen = new Citizen(command);

        var savedCitizen = citizenRepository.save(citizen);
        return Optional.of(savedCitizen);
    }

    @Override
    public Optional<Citizen> handle(UpdateCitizenCommand command) {
        var existingCitizen = citizenRepository.findById(command.citizenId())
                .orElseThrow(() -> new IllegalArgumentException("Citizen with ID " + command.citizenId() + " not found."));

        existingCitizen.update(command);

        var updatedCitizen = citizenRepository.save(existingCitizen);
        return Optional.of(updatedCitizen);
    }

    @Override
    public Boolean handle(DeleteCitizenCommand command) {
        var existingCitizen = citizenRepository.findById(command.citizenId())
                .orElseThrow(() -> new IllegalArgumentException("Citizen with ID " + command.citizenId() + " not found."));

        citizenRepository.delete(existingCitizen);
        return true;
    }
}