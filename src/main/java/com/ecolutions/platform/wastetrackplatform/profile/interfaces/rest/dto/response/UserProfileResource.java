package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record UserProfileResource(
    String id,
    String userId,
    String photoUrl,
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
    String createdAt,
    String updatedAt
) {
}