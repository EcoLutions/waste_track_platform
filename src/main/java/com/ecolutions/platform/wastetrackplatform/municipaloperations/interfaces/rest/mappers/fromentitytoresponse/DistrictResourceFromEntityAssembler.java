package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.OperationalStatus;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.PlanSnapshot;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.DistrictResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DurationUtils;

public class DistrictResourceFromEntityAssembler {
    public static DistrictResource toResourceFromEntity(District entity) {
        return DistrictResource.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .depotLatitud(Location.latitudeAsStringOrNull(entity.getDepotLocation()))
                .depotLongitude(Location.longitudeAsStringOrNull(entity.getDepotLocation()))
                .disposalLatitude(Location.latitudeAsStringOrNull(entity.getDisposalSiteLocation()))
                .disposalLongitude(Location.longitudeAsStringOrNull(entity.getDisposalSiteLocation()))
                .operationalStatus(OperationalStatus.toStringOrNull(entity.getOperationalStatus()))
                .serviceStartDate(DateTimeUtils.localDateToStringOrNull(entity.getServiceStartDate()))
                .operationStartTime(DateTimeUtils.localTimeToStringOrNull(entity.getOperationStartTime()))
                .operationEndTime(DateTimeUtils.localTimeToStringOrNull(entity.getOperationEndTime()))
                .maxRouteDuration(DurationUtils.durationToStringOrNull(entity.getMaxRouteDuration()))
                .planId(PlanSnapshot.planIdToStringOrNull(entity.getPlanSnapshot()))
                .planName(PlanSnapshot.planNameOrNull(entity.getPlanSnapshot()))
                .maxVehicles(PlanSnapshot.maxVehiclesOrNull(entity.getPlanSnapshot()))
                .maxDrivers(PlanSnapshot.maxDriversOrNull(entity.getPlanSnapshot()))
                .maxContainers(PlanSnapshot.maxContainersOrNull(entity.getPlanSnapshot()))
                .currency(PlanSnapshot.currencyOrNull(entity.getPlanSnapshot()))
                .price(PlanSnapshot.priceAsStringOrNull(entity.getPlanSnapshot()))
                .billingPeriod(PlanSnapshot.billingPeriodOrNull(entity.getPlanSnapshot()))
                .currentVehicleCount(entity.getCurrentVehicleCount())
                .currentDriverCount(entity.getCurrentDriverCount())
                .currentContainerCount(entity.getCurrentContainerCount())
                .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
                .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}