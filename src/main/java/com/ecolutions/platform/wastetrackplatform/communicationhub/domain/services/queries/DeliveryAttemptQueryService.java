package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.DeliveryAttempt;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetDeliveryAttemptByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetAllDeliveryAttemptsQuery;

import java.util.List;
import java.util.Optional;

public interface DeliveryAttemptQueryService {
    Optional<DeliveryAttempt> handle(GetDeliveryAttemptByIdQuery query);
    List<DeliveryAttempt> handle(GetAllDeliveryAttemptsQuery query);
}