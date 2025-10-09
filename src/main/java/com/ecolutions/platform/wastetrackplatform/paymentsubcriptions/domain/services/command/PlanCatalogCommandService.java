package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreatePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.DeletePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdatePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.entities.PlanCatalog;

import java.util.Optional;

public interface PlanCatalogCommandService {
    Optional<PlanCatalog> handle(CreatePlanCatalogCommand command);
    Optional<PlanCatalog> handle(UpdatePlanCatalogCommand command);
    Boolean handle(DeletePlanCatalogCommand command);
}