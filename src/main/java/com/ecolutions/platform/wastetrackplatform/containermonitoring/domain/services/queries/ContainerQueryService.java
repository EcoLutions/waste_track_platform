package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetContainerByIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllContainersQuery;

import java.util.List;
import java.util.Optional;

public interface ContainerQueryService {
    Optional<Container> handle(GetContainerByIdQuery query);
    List<Container> handle(GetAllContainersQuery query);
}