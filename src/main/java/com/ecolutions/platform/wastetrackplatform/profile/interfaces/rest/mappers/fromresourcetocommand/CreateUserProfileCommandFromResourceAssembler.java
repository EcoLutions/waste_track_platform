package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.CreateUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Language;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.UserType;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request.CreateUserProfileResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;

public class CreateUserProfileCommandFromResourceAssembler {
    public static CreateUserProfileCommand toCommandFromResource(CreateUserProfileResource resource) {
        return new CreateUserProfileCommand(
            UserId.of(resource.userId()),
            resource.photoUrl(),
            UserType.fromString(resource.userType()),
            DistrictId.of(resource.districtId()),
            EmailAddress.of(resource.email()),
            PhoneNumber.of(resource.phoneNumber()),
            Language.fromString(resource.language()),
            resource.timezone()
        );
    }
}