package com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerRepository extends JpaRepository<Container, String> {
    List<Container> findByDistrictId_Value(String districtId);
}
