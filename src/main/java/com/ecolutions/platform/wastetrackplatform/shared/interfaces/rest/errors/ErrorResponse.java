package com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.errors;

import java.time.LocalDateTime;

public record ErrorResponse(
        String code,
        String message,
        int status,
        LocalDateTime timestamp
) {}
