package com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerRepository extends JpaRepository<Container, String> {
    List<Container> findAllByDistrictId(DistrictId districtId);
}
