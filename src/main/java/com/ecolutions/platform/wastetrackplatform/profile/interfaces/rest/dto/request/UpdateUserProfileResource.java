package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Language;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.UserType;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;

public record UpdateUserProfileResource(
    String photoUrl,
    UserType userType,
    DistrictId districtId,
    EmailAddress email,
    PhoneNumber phoneNumber,
    Boolean emailNotificationsEnabled,
    Boolean smsNotificationsEnabled,
    Boolean pushNotificationsEnabled,
    Language language,
    String timezone,
    Boolean isActive
) {
}