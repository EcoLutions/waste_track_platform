package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record SetCompletedInitialPasswordResource(
        String userId,
        String email,
        List<String> roles
) {
}
