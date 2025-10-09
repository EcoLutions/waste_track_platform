package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreatePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.CreatePlanCatalogResource;

public class CreatePlanCatalogCommandFromResourceAssembler {
    public static CreatePlanCatalogCommand toCommandFromResource(CreatePlanCatalogResource resource) {
        return new CreatePlanCatalogCommand(
            resource.name(),
            resource.monthlyPriceAmount(),
            resource.monthlyPriceCurrency(),
            resource.maxVehicles(),
            resource.maxDrivers(),
            resource.maxContainers()
        );
    }
}