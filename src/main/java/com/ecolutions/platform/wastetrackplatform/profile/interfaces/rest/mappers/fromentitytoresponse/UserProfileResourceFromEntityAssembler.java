package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates.UserProfile;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response.UserProfileResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class UserProfileResourceFromEntityAssembler {
    public static UserProfileResource toResourceFromEntity(UserProfile entity) {
        return UserProfileResource.builder()
            .id(entity.getId())
            .userId(entity.getUserId() != null ? entity.getUserId().value() : null)
            .photoUrl(entity.getPhoto() != null ? entity.getPhoto().filePath() : null)
            .userType(entity.getUserType() != null ? entity.getUserType().name() : null)
            .districtId(entity.getDistrictId() != null ? entity.getDistrictId().value() : null)
            .email(entity.getEmail() != null ? entity.getEmail().value() : null)
            .phoneNumber(entity.getPhoneNumber() != null ? entity.getPhoneNumber().value() : null)
            .emailNotificationsEnabled(entity.getEmailNotificationsEnabled())
            .smsNotificationsEnabled(entity.getSmsNotificationsEnabled())
            .pushNotificationsEnabled(entity.getPushNotificationsEnabled())
            .language(entity.getLanguage() != null ? entity.getLanguage().name() : null)
            .timezone(entity.getTimezone())
            .isActive(entity.getIsActive())
            .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}