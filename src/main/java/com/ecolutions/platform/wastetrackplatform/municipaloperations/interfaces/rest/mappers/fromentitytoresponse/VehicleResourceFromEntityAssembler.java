package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Vehicle;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.VehicleResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class VehicleResourceFromEntityAssembler {
    public static VehicleResource toResourceFromEntity(Vehicle entity) {
        return VehicleResource.builder()
                .id(entity.getId())
                .licensePlate(LicensePlate.toStringOrNull(entity.getLicensePlate()))
                .vehicleType(VehicleType.toStringOrNull(entity.getVehicleType()))
                .volumeCapacity(VolumeCapacity.toStringOrNull(entity.getVolumeCapacity()))
                .weightCapacity(WeightCapacity.toIntegerOrNull(entity.getWeightCapacity()))
                .mileage(Mileage.toIntegerOrNull(entity.getMileage()))
                .districtId(DistrictId.toStringOrNull(entity.getDistrictId()))
                .lastMaintenanceDate(DateTimeUtils.localDateTimeToStringOrNull(entity.getLastMaintenanceDate()))
                .nextMaintenanceDate(DateTimeUtils.localDateTimeToStringOrNull(entity.getNextMaintenanceDate()))
                .isActive(entity.getIsActive())
                .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
                .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
                .build();
    }
}