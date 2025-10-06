package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.SensorReading;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.SensorReadingResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class SensorReadingResourceFromEntityAssembler {
    public static SensorReadingResource toResourceFromEntity(SensorReading entity) {
        return SensorReadingResource.builder()
            .id(entity.getId())
            .containerId(ContainerId.toStringOrNull(entity.getContainerId()))
            .fillLevelPercentage(entity.getFillLevel() != null ? entity.getFillLevel().percentage() : null)
            .temperatureCelsius(entity.getTemperature() != null ? entity.getTemperature().celsius().doubleValue() : null)
            .batteryLevelPercentage(entity.getBatteryLevel() != null ? entity.getBatteryLevel().percentage() : null)
            .validationStatus(entity.getValidationStatus() != null ? entity.getValidationStatus().name() : null)
            .recordedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getRecordedAt()))
            .receivedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getReceivedAt()))
            .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}