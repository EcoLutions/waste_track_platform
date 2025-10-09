package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.entities.PlanCatalog;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetAllPlanCatalogsQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetPlanCatalogByIdQuery;

import java.util.List;
import java.util.Optional;

public interface PlanCatalogQueryService {
    Optional<PlanCatalog> handle(GetPlanCatalogByIdQuery query);
    List<PlanCatalog> handle(GetAllPlanCatalogsQuery query);
}