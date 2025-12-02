package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.outboundservices.acl.ExternalPaymentSubscriptionsService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.events.DistrictCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.PlanSnapshot;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command.DistrictCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories.DistrictRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictCommandServiceImpl implements DistrictCommandService {
    private final DistrictRepository districtRepository;
    private final ExternalPaymentSubscriptionsService externalPaymentSubscriptionsService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Optional<District> handle(CreateDistrictCommand command) {
        if (districtRepository.existsByCode(command.code()))
            throw new IllegalArgumentException("District with code " + command.code() + " already exists.");

        PlanSnapshot planSnapshot = externalPaymentSubscriptionsService.getPlanInfo(command.planId())
                .orElseThrow(() -> new IllegalArgumentException("Plan with ID " + command.planId() + " not found."));

        District newDistrict = new District(command);
        newDistrict.updatePlanSnapshot(planSnapshot);

        District savedDistrict = districtRepository.save(newDistrict);

        DistrictCreatedEvent event = savedDistrict.publishDistrictCreatedEvent(
                command.primaryAdminEmail().value(),
                command.primaryAdminUsername().value(),
                command.planId()
        );
        eventPublisher.publishEvent(event);

        return Optional.of(savedDistrict);
    }

    @Override
    @Transactional
    public Optional<District> handle(UpdateDistrictCommand command) {
        District existingDistrict = districtRepository.findById(command.districtId())
                .orElseThrow(() -> new IllegalArgumentException("District with ID " + command.districtId() + " not found."));

        if(districtRepository.existsByCode(command.code()) && !existingDistrict.getCode().equals(command.code()))
            throw new IllegalArgumentException("Another district with code " + command.code() + " already exists.");

        existingDistrict.update(command);
        var updatedDistrict = districtRepository.save(existingDistrict);
        return Optional.of(updatedDistrict);
    }

    @Override
    @Transactional
    public Boolean handle(DeleteDistrictCommand command) {
        District existingDistrict = districtRepository.findById(command.districtId())
                .orElseThrow(() -> new IllegalArgumentException("District with ID " + command.districtId() + " not found."));
        districtRepository.delete(existingDistrict);
        return Boolean.TRUE;
    }
}