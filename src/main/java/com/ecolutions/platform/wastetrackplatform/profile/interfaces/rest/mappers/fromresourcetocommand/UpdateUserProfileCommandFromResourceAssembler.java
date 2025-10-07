package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.UpdateUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Language;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.UserType;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request.UpdateUserProfileResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;

public class UpdateUserProfileCommandFromResourceAssembler {
    public static UpdateUserProfileCommand toCommandFromResource(String userProfileId, UpdateUserProfileResource resource) {
        return new UpdateUserProfileCommand(
            userProfileId,
            resource.photoUrl(),
            UserType.fromString(resource.userType()),
            DistrictId.of(resource.districtId()),
            EmailAddress.of(resource.email()),
            PhoneNumber.of(resource.phoneNumber()),
            resource.emailNotificationsEnabled(),
            resource.smsNotificationsEnabled(),
            resource.pushNotificationsEnabled(),
            Language.fromString(resource.language()),
            resource.timezone(),
            resource.isActive()
        );
    }
}