package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Driver;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDriversByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDriversQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetCurrentDriverQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetDriverByIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries.DriverQueryService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories.DriverRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverQueryServiceImpl implements DriverQueryService {
    private final DriverRepository driverRepository;

    @Override
    public Optional<Driver> handle(GetDriverByIdQuery query) {
        try {
            return driverRepository.findById(query.driverId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve driver: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Driver> handle(GetAllDriversQuery query) {
        try {
            return driverRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve drivers: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Driver> handle(GetAllDriversByDistrictIdQuery query) {
        try {
            return driverRepository.findByDistrictId(new DistrictId(query.districtId()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve drivers by districtId: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Driver> handle(GetCurrentDriverQuery query) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                return driverRepository.findByEmailAddress(new EmailAddress(email));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve current driver: " + e.getMessage(), e);
        }
    }
}