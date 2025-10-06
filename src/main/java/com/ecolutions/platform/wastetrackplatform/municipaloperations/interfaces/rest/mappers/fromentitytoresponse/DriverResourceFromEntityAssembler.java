package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Driver;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.DriverLicense;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.DriverStatus;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.response.DriverResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class DriverResourceFromEntityAssembler {
    public static DriverResource toResourceFromEntity(Driver entity) {
        return DriverResource.builder()
            .id(entity.getId())
            .districtId(DistrictId.toStringOrNull(entity.getDistrictId()))
            .firstName(FullName.firstNameOrNull(entity.getFullName()))
            .lastName(FullName.lastNameOrNull(entity.getFullName()))
            .documentNumber(DocumentNumber.toStringOrNull(entity.getDocumentNumber()))
            .phoneNumber(PhoneNumber.toStringOrNull(entity.getPhoneNumber()))
            .userId(UserId.toStringOrNull(entity.getUserId()))
            .driverLicense(DriverLicense.toStringOrNull(entity.getDriverLicense()))
            .licenseExpiryDate(DateTimeUtils.localDateToStringOrNull(entity.getLicenseExpiryDate()))
            .emailAddress(EmailAddress.toStringOrNull(entity.getEmailAddress()))
            .totalHoursWorked(entity.getTotalHoursWorked())
            .lastRouteCompletedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getLastRouteCompletedAt()))
            .status(DriverStatus.toStringOrNull(entity.getStatus()))
            .assignedVehicleId(VehicleId.toStringOrNull(entity.getAssignedVehicleId()))
            .build();
    }
}