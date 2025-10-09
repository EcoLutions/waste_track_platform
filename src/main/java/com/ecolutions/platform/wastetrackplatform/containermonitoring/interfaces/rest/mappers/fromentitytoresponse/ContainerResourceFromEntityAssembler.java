package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.SensorId;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.ContainerResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class ContainerResourceFromEntityAssembler {
    public static ContainerResource toResourceFromEntity(Container entity) {
        return ContainerResource.builder()
            .id(entity.getId())
            .latitude(entity.getLocation() != null && entity.getLocation().latitude() != null ?
                     entity.getLocation().latitude().toString() : null)
            .longitude(entity.getLocation() != null && entity.getLocation().longitude() != null ?
                      entity.getLocation().longitude().toString() : null)
            .address(entity.getLocation() != null ? entity.getLocation().address() : null)
            .districtCode(entity.getLocation() != null ? entity.getLocation().districtCode() : null)
            .volumeLiters(entity.getCapacity() != null ? entity.getCapacity().volumeLiters() : null)
            .maxWeightKg(entity.getCapacity() != null ? entity.getCapacity().maxWeightKg() : null)
            .containerType(entity.getContainerType() != null ? entity.getContainerType().name() : null)
            .status(entity.getStatus() != null ? entity.getStatus().name() : null)
            .currentFillLevel(entity.getCurrentFillLevel() != null ? entity.getCurrentFillLevel().percentage() : null)
            .sensorId(SensorId.toStringOrNull(entity.getSensorId()))
            .lastReadingTimestamp(DateTimeUtils.localDateTimeToStringOrNull(entity.getLastReadingTimestamp()))
            .districtId(DistrictId.toStringOrNull(entity.getDistrictId()))
            .lastCollectionDate(DateTimeUtils.localDateTimeToStringOrNull(entity.getLastCollectionDate()))
            .collectionFrequencyDays(entity.getCollectionFrequency() != null ? entity.getCollectionFrequency().days() : null)
            .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}