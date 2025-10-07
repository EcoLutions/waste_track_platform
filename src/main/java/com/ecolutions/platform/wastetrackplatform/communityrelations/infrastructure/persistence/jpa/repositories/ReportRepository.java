package com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
}