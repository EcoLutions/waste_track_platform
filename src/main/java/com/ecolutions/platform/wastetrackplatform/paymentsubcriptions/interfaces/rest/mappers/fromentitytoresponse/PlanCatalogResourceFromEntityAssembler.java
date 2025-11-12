package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.entities.PlanCatalog;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.BillingPeriod;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.PlanCatalogResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;

public class PlanCatalogResourceFromEntityAssembler {
    public static PlanCatalogResource toResourceFromEntity(PlanCatalog entity) {
        return PlanCatalogResource.builder()
            .id(entity.getId())
            .name(entity.getName())
            .priceAmount(Money.amountAsStringOrNull(entity.getPrice()))
            .priceCurrency(Money.currencyOrNull(entity.getPrice()))
            .billingPeriod(BillingPeriod.toStringOrNull(entity.getBillingPeriod()))
            .maxVehicles(entity.getMaxVehicles())
            .maxDrivers(entity.getMaxDrivers())
            .maxContainers(entity.getMaxContainers())
            .build();
    }
}