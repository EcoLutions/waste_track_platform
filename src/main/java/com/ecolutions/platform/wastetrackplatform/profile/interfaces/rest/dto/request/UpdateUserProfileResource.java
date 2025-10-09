package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request;

public record UpdateUserProfileResource(
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
    Boolean isActive
) {
}