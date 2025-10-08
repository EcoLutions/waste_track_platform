package com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects;

public record AuthenticationResult(boolean isAuthenticated, String message, String userId) {
    public AuthenticationResult {
        if (message == null) {
            message = "";
        }
    }

    public static AuthenticationResult success(String userId) {
        return new AuthenticationResult(true, "Authentication successful", userId);
    }

    public static AuthenticationResult failure(String message) {
        return new AuthenticationResult(false, message, null);
    }

    public static AuthenticationResult accountLocked() {
        return new AuthenticationResult(false, "Account is locked", null);
    }

    public static AuthenticationResult accountDisabled() {
        return new AuthenticationResult(false, "Account is disabled", null);
    }

    public static AuthenticationResult pendingActivation() {
        return new AuthenticationResult(false, "Account is pending activation", null);
    }
}