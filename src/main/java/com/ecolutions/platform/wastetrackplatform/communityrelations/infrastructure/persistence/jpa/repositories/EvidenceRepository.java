package com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.entities.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, String> {
    @Query(value = "SELECT * FROM evidences WHERE report_id = :reportId", nativeQuery = true)
    List<Evidence> findAllByReportId(@Param("reportId") String reportId);
}
