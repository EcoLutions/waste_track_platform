package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.acl;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetDistrictByIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.ValidateDistrictScheduleTimeQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries.DistrictQueryService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts.MunicipalOperationsContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.mappers.DistrictConfigDTOFromEntityAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MunicipalOperationsContextFacadeImpl implements MunicipalOperationsContextFacade {
    private final DistrictQueryService districtQueryService;

    @Override
    public Optional<DistrictConfigDTO> getDistrictConfiguration(String districtId) {
        var query = new GetDistrictByIdQuery(districtId);
        var district = districtQueryService.handle(query);
        if (district.isEmpty()) return Optional.empty();
        var dto = DistrictConfigDTOFromEntityAssembler.toDtoFromEntity(district.get());
        return Optional.of(dto);
    }

    @Override
    public boolean validateScheduledTime(String districtId, LocalDateTime scheduledStartAt) {
        var query = new ValidateDistrictScheduleTimeQuery(districtId, scheduledStartAt);
        return districtQueryService.handle(query);
    }
}
