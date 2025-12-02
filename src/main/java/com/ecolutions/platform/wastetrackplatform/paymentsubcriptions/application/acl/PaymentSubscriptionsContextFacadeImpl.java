package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.application.acl;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetPlanCatalogByIdQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries.PlanCatalogQueryService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.contexts.PaymentSubscriptionsContextFacade;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.dtos.PlanInfoDTO;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.mappers.PlanInfoDTOFromEntityAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentSubscriptionsContextFacadeImpl implements PaymentSubscriptionsContextFacade {
    private final PlanCatalogQueryService planCatalogQueryService;

    @Override
    public Optional<PlanInfoDTO> getPlanInfo(String planId) {
        var query = new GetPlanCatalogByIdQuery(planId);
        var planCatalog = planCatalogQueryService.handle(query);
        if (planCatalog.isEmpty()) return Optional.empty();
        var dto = PlanInfoDTOFromEntityAssembler.toDtoFromEntity(planCatalog.get());
        return Optional.of(dto);
    }
}
