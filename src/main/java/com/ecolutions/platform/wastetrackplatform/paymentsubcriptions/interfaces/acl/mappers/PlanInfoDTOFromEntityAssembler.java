package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.mappers;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.entities.PlanCatalog;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.dtos.PlanInfoDTO;

public class PlanInfoDTOFromEntityAssembler {
    public static PlanInfoDTO toDtoFromEntity(PlanCatalog entity) {
        return new PlanInfoDTO(
                entity.getId(),
                entity.getName(),
                entity.getMaxVehicles(),
                entity.getMaxDrivers(),
                entity.getMaxContainers(),
                entity.getPrice().amount(),
                entity.getPrice().currency(),
                entity.getBillingPeriod().name()
        );
    }
}
