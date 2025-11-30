package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Device;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.DeviceIdentifier;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.DeviceResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class DeviceResourceFromEntityAssembler {
    public static DeviceResource toResourceFromEntity(Device entity) {
        return DeviceResource.builder()
                .id(entity.getId())
                .deviceIdentifier(DeviceIdentifier.toStringOrNull(entity.getDeviceIdentifier()))
                .isOnline(entity.getIsOnline())
                .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
                .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
                .build();
    }
}
