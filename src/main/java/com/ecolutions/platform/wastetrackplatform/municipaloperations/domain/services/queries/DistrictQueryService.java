package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllDistrictsQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetDistrictByIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.ValidateDistrictScheduleTimeQuery;

import java.util.List;
import java.util.Optional;

public interface DistrictQueryService {
    Optional<District> handle(GetDistrictByIdQuery query);
    List<District> handle(GetAllDistrictsQuery query);
    boolean handle(ValidateDistrictScheduleTimeQuery query);
}