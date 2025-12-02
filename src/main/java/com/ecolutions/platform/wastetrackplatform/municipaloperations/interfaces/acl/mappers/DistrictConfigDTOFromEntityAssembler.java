package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.mappers;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;

public class DistrictConfigDTOFromEntityAssembler {
    public static DistrictConfigDTO toDtoFromEntity(District district) {
        return new DistrictConfigDTO(
                district.getId(),
                district.getName(),
                district.getMaxRouteDuration(),
                district.getOperationStartTime(),
                district.getOperationEndTime(),
                district.getDepotLocation() != null ? district.getDepotLocation().latitude() : null,
                district.getDepotLocation() != null ? district.getDepotLocation().longitude() : null,
                district.getDisposalSiteLocation() != null ? district.getDisposalSiteLocation().latitude() : null,
                district.getDisposalSiteLocation() != null ? district.getDisposalSiteLocation().longitude() : null
        );
    }
}
