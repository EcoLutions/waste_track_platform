package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Vehicle;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllVehiclesQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetVehicleByIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries.VehicleQueryService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleQueryServiceImpl implements VehicleQueryService {
    private final VehicleRepository vehicleRepository;

    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        try {
            return vehicleRepository.findById(query.vehicleId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve vehicle: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehicle> handle(GetAllVehiclesQuery query) {
        try {
            return vehicleRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve vehicles: " + e.getMessage(), e);
        }
    }
}