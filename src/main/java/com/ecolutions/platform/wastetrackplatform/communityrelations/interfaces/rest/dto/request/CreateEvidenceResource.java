package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CreateEvidenceResource(
    String description,
    MultipartFile file
) {
    public CreateEvidenceResource {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }
    }
}