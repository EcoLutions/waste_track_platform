package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDistrictsQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetDistrictByIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries.DistrictQueryService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictQueryServiceImpl implements DistrictQueryService {
    private final DistrictRepository districtRepository;

    @Override
    public Optional<District> handle(GetDistrictByIdQuery query) {
        try {
            return districtRepository.findById(query.districtId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve district: " + e.getMessage(), e);
        }
    }

    @Override
    public List<District> handle(GetAllDistrictsQuery query) {
        try {
            return districtRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve districts: " + e.getMessage(), e);
        }
    }
}