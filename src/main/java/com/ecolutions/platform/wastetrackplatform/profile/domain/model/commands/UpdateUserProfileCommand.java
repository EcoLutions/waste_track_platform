package com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Language;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.UserType;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;

public record UpdateUserProfileCommand(
    String userProfileId,
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
    public UpdateUserProfileCommand {
        if (userProfileId == null || userProfileId.isBlank()) {
            throw new IllegalArgumentException("User Profile ID cannot be null or blank");
        }
        // Other fields are optional for update
    }
}