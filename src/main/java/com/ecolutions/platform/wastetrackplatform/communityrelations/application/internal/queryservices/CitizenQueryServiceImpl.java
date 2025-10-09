package com.ecolutions.platform.wastetrackplatform.communityrelations.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Citizen;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetCitizenByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllCitizensQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries.CitizenQueryService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories.CitizenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitizenQueryServiceImpl implements CitizenQueryService {
    private final CitizenRepository citizenRepository;

    @Override
    public Optional<Citizen> handle(GetCitizenByIdQuery query) {
        try {
            return citizenRepository.findById(query.citizenId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve citizen: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Citizen> handle(GetAllCitizensQuery query) {
        try {
            return citizenRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve citizens: " + e.getMessage(), e);
        }
    }
}