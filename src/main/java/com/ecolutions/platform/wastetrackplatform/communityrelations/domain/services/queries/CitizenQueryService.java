package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Citizen;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllCitizensByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetCitizenByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllCitizensQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetCitizenByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface CitizenQueryService {
    Optional<Citizen> handle(GetCitizenByIdQuery query);
    Optional<Citizen> handle(GetCitizenByUserIdQuery query);
    List<Citizen> handle(GetAllCitizensQuery query);
    List<Citizen> handle(GetAllCitizensByDistrictIdQuery query);
}