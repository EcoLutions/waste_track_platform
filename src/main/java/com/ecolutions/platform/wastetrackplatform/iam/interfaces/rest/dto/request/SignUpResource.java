package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request;

import java.util.List;

public record SignUpResource(String email, String password, List<String> roles) {
}
