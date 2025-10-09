package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdatePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.UpdatePlanCatalogResource;

public class UpdatePlanCatalogCommandFromResourceAssembler {
    public static UpdatePlanCatalogCommand toCommandFromResource(String planCatalogId, UpdatePlanCatalogResource resource) {
        return new UpdatePlanCatalogCommand(
                planCatalogId,
                resource.name(),
                resource.monthlyPriceAmount(),
                resource.monthlyPriceCurrency(),
                resource.maxVehicles(),
                resource.maxDrivers(),
                resource.maxContainers()
        );
    }
}
