package com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Driver;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
    List<Driver> findByDistrictId(DistrictId districtId);
}
