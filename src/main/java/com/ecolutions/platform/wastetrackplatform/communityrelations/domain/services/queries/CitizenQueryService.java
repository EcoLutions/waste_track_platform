package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Citizen;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllCitizensByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetCitizenByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllCitizensQuery;

import java.util.List;
import java.util.Optional;

public interface CitizenQueryService {
    Optional<Citizen> handle(GetCitizenByIdQuery query);
    List<Citizen> handle(GetAllCitizensQuery query);
    List<Citizen> handle(GetAllCitizensByDistrictIdQuery query);
}