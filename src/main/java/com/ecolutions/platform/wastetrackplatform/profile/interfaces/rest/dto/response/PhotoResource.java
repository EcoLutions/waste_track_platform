package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record PhotoResource(
    String filePath
) {
}