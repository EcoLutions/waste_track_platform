package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response;

public record AuthenticatedUserResource(String id, String email, String token) {
}
