package com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Vehicle;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByDistrictId(DistrictId districtId);
}