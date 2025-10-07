package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record EvidenceResource(
    String id,
    String type,
    String originalFileName,
    String fileUrl,
    String thumbnailUrl,
    String description,
    Long fileSize,
    String mimeType,
    String createdAt
) {
}