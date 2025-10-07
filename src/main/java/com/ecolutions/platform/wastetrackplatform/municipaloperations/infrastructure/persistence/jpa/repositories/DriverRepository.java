package com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
}
