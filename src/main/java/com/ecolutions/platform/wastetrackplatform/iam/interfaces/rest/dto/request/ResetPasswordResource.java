package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request;

public record ResetPasswordResource(
        String resetToken,
        String newPassword
) {
}
