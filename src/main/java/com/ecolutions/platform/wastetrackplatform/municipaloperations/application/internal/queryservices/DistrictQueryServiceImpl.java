package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDistrictsQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetDistrictByIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.ValidateDistrictScheduleTimeQuery;
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
        return districtRepository.findById(query.districtId());
    }

    @Override
    public List<District> handle(GetAllDistrictsQuery query) {
        return districtRepository.findAll();
    }

    @Override
    public boolean handle(ValidateDistrictScheduleTimeQuery query) {
        var district = districtRepository.findById(query.districtId())
                .orElseThrow(() -> new IllegalArgumentException("District not found: " + query.districtId()));
        if (district.getOperationStartTime() == null || district.getOperationEndTime() == null) return true;
        return district.isWithinOperatingHours(query.startedAt().toLocalTime());
    }
}