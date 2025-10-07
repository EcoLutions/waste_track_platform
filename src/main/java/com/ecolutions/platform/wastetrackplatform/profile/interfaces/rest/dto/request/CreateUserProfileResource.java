package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request;

public record CreateUserProfileResource(
    String userId,
    String photoUrl,
    String userType,
    String districtId,
    String email,
    String phoneNumber,
    String language,
    String timezone
) {
    public CreateUserProfileResource {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (userType == null || userType.isBlank()) {
            throw new IllegalArgumentException("User type cannot be null or blank");
        }
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("Language cannot be null or blank");
        }
        if (timezone == null || timezone.isBlank()) {
            throw new IllegalArgumentException("Timezone cannot be null or blank");
        }
    }
}