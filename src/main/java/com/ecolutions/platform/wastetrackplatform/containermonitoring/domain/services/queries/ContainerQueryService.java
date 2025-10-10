package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllContainersByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetContainerByIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllContainersQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetContainersInAlertByDistrictIdQuery;

import java.util.List;
import java.util.Optional;

public interface ContainerQueryService {
    Optional<Container> handle(GetContainerByIdQuery query);
    List<Container> handle(GetAllContainersQuery query);
    List<Container> handle(GetAllContainersByDistrictIdQuery query);
    List<Container> handle(GetContainersInAlertByDistrictIdQuery query);
}