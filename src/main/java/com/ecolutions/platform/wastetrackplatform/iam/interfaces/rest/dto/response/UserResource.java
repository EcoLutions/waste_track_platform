package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response;

import java.util.List;

public record UserResource(String id, String username, List<String> roles) {
}
