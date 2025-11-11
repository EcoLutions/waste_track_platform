package com.tamci.equipmentservicemanagerplatform.iam.interfaces.rest.dto.request;

public record ResetPasswordResource(
        String resetToken,
        String newPassword
) {
}
