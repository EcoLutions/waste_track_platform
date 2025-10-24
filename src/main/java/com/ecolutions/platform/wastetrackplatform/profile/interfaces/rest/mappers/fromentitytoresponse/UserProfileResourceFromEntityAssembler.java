package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates.UserProfile;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Language;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Photo;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response.UserProfileResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class UserProfileResourceFromEntityAssembler {

    public static UserProfileResource toResourceFromEntity(UserProfile entity, String photoUrl) {
        return UserProfileResource.builder()
            .id(entity.getId())
            .userId(UserId.toStringOrNull(entity.getUserId()))
            .photoPath(Photo.toStringOrNull(entity.getPhoto()))
            .districtId(DistrictId.toStringOrNull(entity.getDistrictId()))
            .email(EmailAddress.toStringOrNull(entity.getEmail()))
            .phoneNumber(PhoneNumber.toStringOrNull(entity.getPhoneNumber()))
            .emailNotificationsEnabled(entity.getEmailNotificationsEnabled())
            .smsNotificationsEnabled(entity.getSmsNotificationsEnabled())
            .pushNotificationsEnabled(entity.getPushNotificationsEnabled())
            .language(Language.toStringOrNull(entity.getLanguage()))
            .timezone(entity.getTimezone())
            .temporalPhotoUrl(photoUrl) // URL temporal generada
            .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
            .build();
    }
}