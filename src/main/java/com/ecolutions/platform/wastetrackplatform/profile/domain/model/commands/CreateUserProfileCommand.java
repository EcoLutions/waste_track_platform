package com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Language;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;

public record CreateUserProfileCommand(
    UserId userId,
    String photoUrl,
    DistrictId districtId,
    EmailAddress email,
    PhoneNumber phoneNumber,
    Language language,
    String timezone
) {
    public CreateUserProfileCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (language == null) {
            throw new IllegalArgumentException("Language cannot be null");
        }
        if (timezone == null || timezone.isBlank()) {
            throw new IllegalArgumentException("Timezone cannot be null or blank");
        }
    }
}