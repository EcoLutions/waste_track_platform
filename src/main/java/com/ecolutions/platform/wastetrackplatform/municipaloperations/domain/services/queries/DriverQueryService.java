package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Driver;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDriversByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDriversQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetCurrentDriverQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetDriverByIdQuery;

import java.util.List;
import java.util.Optional;

public interface DriverQueryService {
    Optional<Driver> handle(GetDriverByIdQuery query);
    List<Driver> handle(GetAllDriversQuery query);
    List<Driver> handle(GetAllDriversByDistrictIdQuery query);
    Optional<Driver> handle(GetCurrentDriverQuery query);
}