package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record UserProfileResource(
    String id,
    String userId,
    String photoPath,
    String userType,
    String districtId,
    String email,
    String phoneNumber,
    Boolean emailNotificationsEnabled,
    Boolean smsNotificationsEnabled,
    Boolean pushNotificationsEnabled,
    String language,
    String timezone,
    Boolean isActive,
    String temporalPhotoUrl,
    String createdAt,
    String updatedAt
) {
}