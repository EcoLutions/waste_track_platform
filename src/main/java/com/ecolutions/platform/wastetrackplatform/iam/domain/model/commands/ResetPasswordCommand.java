package com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands;

public record ResetPasswordCommand(String token, String password) {
}
