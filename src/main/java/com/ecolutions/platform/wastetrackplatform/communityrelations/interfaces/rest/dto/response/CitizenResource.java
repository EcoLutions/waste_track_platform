package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record CitizenResource(
    String id,
    String userId,
    String districtId,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String membershipLevel,
    Integer totalPoints,
    Integer totalReportsSubmitted,
    String lastActivityDate,
    String createdAt,
    String updatedAt
) {
}