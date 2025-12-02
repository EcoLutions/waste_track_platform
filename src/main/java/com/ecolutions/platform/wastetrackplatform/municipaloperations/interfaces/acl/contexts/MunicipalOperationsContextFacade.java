package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MunicipalOperationsContextFacade {
    Optional<DistrictConfigDTO> getDistrictConfiguration(String districtId);
    boolean validateScheduledTime(String districtId, LocalDateTime scheduledStartAt);
}
