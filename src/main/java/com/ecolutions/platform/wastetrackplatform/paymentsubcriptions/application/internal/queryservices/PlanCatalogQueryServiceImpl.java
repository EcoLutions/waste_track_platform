package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.entities.PlanCatalog;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetAllPlanCatalogsQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetPlanCatalogByIdQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries.PlanCatalogQueryService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories.PlanCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanCatalogQueryServiceImpl implements PlanCatalogQueryService {
    private final PlanCatalogRepository planCatalogRepository;

    @Override
    public Optional<PlanCatalog> handle(GetPlanCatalogByIdQuery query) {
        try {
            return planCatalogRepository.findById(query.planCatalogId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve plan catalog: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PlanCatalog> handle(GetAllPlanCatalogsQuery query) {
        try {
            return planCatalogRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve plan catalogs: " + e.getMessage(), e);
        }
    }
}