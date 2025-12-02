package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.SensorReading;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.BatteryLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.CurrentFillLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.Temperature;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.ValidationStatus;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response.SensorReadingResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class SensorReadingResourceFromEntityAssembler {
    public static SensorReadingResource toResourceFromEntity(SensorReading entity) {
        return SensorReadingResource.builder()
            .id(entity.getId())
            .containerId(ContainerId.toStringOrNull(entity.getContainerId()))
            .fillLevelPercentage(CurrentFillLevel.toIntegerOrNull(entity.getFillLevel()))
            .temperatureCelsius(Temperature.toStringOrNull(entity.getTemperature()))
            .batteryLevelPercentage(BatteryLevel.toIntegerOrNull(entity.getBatteryLevel()))
            .validationStatus(ValidationStatus.toStringOrNull(entity.getValidationStatus()))
            .recordedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getRecordedAt()))
            .receivedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getReceivedAt()))
            .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}