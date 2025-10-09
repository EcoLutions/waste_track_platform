package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.entities.PlanCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanCatalogRepository extends JpaRepository<PlanCatalog, String> {
}