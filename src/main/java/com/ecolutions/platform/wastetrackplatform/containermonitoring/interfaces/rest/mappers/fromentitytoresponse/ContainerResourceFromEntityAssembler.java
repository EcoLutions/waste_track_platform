package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.ContainerResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DeviceId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class ContainerResourceFromEntityAssembler {
    public static ContainerResource toResourceFromEntity(Container entity) {
        return ContainerResource.builder()
            .id(entity.getId())
            .latitude(Location.latitudeAsStringOrNull(entity.getLocation()))
            .longitude(Location.longitudeAsStringOrNull(entity.getLocation()))
            .volumeLiters(ContainerCapacity.volumeLitersToIntegerOrNull(entity.getCapacity()))
            .maxFillLevel(ContainerCapacity.maxFillLevelToIntegerOrNull(entity.getCapacity()))
            .containerType(ContainerType.toStringOrNull(entity.getContainerType()))
            .status(ContainerStatus.toStringOrNull(entity.getStatus()))
            .currentFillLevel(CurrentFillLevel.toIntegerOrNull(entity.getCurrentFillLevel()))
            .deviceId(DeviceId.toStringOrNull(entity.getDeviceId()))
            .lastReadingTimestamp(DateTimeUtils.localDateTimeToStringOrNull(entity.getLastReadingTimestamp()))
            .districtId(DistrictId.toStringOrNull(entity.getDistrictId()))
            .lastCollectionDate(DateTimeUtils.localDateTimeToStringOrNull(entity.getLastCollectionDate()))
            .collectionFrequencyDays(CollectionFrequency.toIntegerOrNull(entity.getCollectionFrequency()))
            .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}