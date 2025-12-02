package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request;

public record UpdateUserProfileResource(
    String photoPath,
    String districtId,
    String email,
    String phoneNumber,
    Boolean emailNotificationsEnabled,
    Boolean smsNotificationsEnabled,
    Boolean pushNotificationsEnabled,
    String language,
    String timezone
) {
}