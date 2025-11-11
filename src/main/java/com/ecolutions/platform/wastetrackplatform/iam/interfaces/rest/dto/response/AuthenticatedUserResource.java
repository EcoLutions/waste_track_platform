package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record AuthenticatedUserResource(String id, String email, String username, String token) {
}
