package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.entities.Evidence;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllEvidencesQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetEvidenceByIdQuery;

import java.util.List;
import java.util.Optional;

public interface EvidenceQueryService {
    Optional<Evidence> handle(GetEvidenceByIdQuery query);
    List<Evidence> handle(GetAllEvidencesQuery query);
}