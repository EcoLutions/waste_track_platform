package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.entities.Evidence;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response.EvidenceResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class EvidenceResourceFromEntityAssembler {

    public static EvidenceResource toResourceFromEntity(Evidence evidence) {
        return EvidenceResource.builder()
            .id(evidence.getId())
            .type(evidence.getType().name())
            .originalFileName(evidence.getOriginalFileName())
            .description(evidence.getDescription())
            .fileSize(evidence.getFileSize())
            .mimeType(evidence.getMimeType())
            .createdAt(DateTimeUtils.dateToStringOrNull(evidence.getCreatedAt()))
            .build();
    }

    // Método especial que incluye URL temporal generada en tiempo real
    public static EvidenceResource toResourceWithUrl(Evidence evidence, String fileUrl) {
        return EvidenceResource.builder()
            .id(evidence.getId())
            .type(evidence.getType().name())
            .originalFileName(evidence.getOriginalFileName())
            .fileUrl(fileUrl)  // ← URL temporal fresca
            .description(evidence.getDescription())
            .fileSize(evidence.getFileSize())
            .mimeType(evidence.getMimeType())
            .createdAt(DateTimeUtils.dateToStringOrNull(evidence.getCreatedAt()))
            .build();
    }
}