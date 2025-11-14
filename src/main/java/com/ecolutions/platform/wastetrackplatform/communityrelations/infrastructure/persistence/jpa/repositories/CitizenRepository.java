package com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Citizen;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, String> {
    List<Citizen> findByDistrictId(DistrictId districtId);
    boolean existsByUserId(UserId userId);
    Optional<Citizen> findByUserId(UserId userId);
}