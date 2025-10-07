package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Citizen;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.RewardPoints;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response.CitizenResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class CitizenResourceFromEntityAssembler {
    public static CitizenResource toResourceFromEntity(Citizen entity) {
        return CitizenResource.builder()
            .id(entity.getId())
            .userId(UserId.toStringOrNull(entity.getUserId()))
            .districtId(DistrictId.toStringOrNull(entity.getDistrictId()))
            .firstName(FullName.firstNameOrNull(entity.getFullName()))
            .lastName(FullName.lastNameOrNull(entity.getFullName()))
            .email(EmailAddress.toStringOrNull(entity.getEmail()))
            .phoneNumber(PhoneNumber.toStringOrNull(entity.getPhoneNumber()))
            .membershipLevel(entity.getMembershipLevel() != null ? entity.getMembershipLevel().name() : null)
            .totalPoints(RewardPoints.toIntegerOrNull(entity.getTotalPoints()))
            .totalReportsSubmitted(entity.getTotalReportsSubmitted())
            .lastActivityDate(DateTimeUtils.localDateTimeToStringOrNull(entity.getLastActivityDate()))
            .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}