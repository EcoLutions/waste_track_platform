package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.DistrictResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.SubscriptionId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class DistrictResourceFromEntityAssembler {
    public static DistrictResource toResourceFromEntity(District entity) {
        return DistrictResource.builder()
            .id(entity.getId())
            .name(entity.getName())
            .code(entity.getCode())
            .boundaries(entity.getBoundaries() != null ? entity.getBoundaries().boundaryPolygon() : null)
            .operationalStatus(entity.getOperationalStatus().name())
            .serviceStartDate(DateTimeUtils.localDateToStringOrNull(entity.getServiceStartDate()))
            .subscriptionId(SubscriptionId.toStringOrNull(entity.getSubscriptionSnapshot().subscriptionId()))
            .planName(entity.getSubscriptionSnapshot().planName())
            .maxVehicles(entity.getSubscriptionSnapshot().maxVehicles())
            .maxDrivers(entity.getSubscriptionSnapshot().maxDrivers())
            .maxContainers(entity.getSubscriptionSnapshot().maxContainers())
            .subscriptionStartedAt(DateTimeUtils.localDateToStringOrNull(entity.getSubscriptionSnapshot().startedAt()))
            .subscriptionEndedAt(DateTimeUtils.localDateToStringOrNull(entity.getSubscriptionSnapshot().endedAt()))
            .primaryAdminEmail(EmailAddress.toStringOrNull(entity.getPrimaryAdminEmail()))
            .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}