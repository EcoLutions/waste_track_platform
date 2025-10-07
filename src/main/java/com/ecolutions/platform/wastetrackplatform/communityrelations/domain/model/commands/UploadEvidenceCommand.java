package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

public record UploadEvidenceCommand(
    String description,
    MultipartFile file,
    String contentType,
    String originalFileName,
    Long fileSize
) {
    public UploadEvidenceCommand {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }
        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("Content type cannot be null or blank");
        }
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("Original file name cannot be null or blank");
        }
        if (fileSize == null || fileSize <= 0) {
            throw new IllegalArgumentException("File size must be greater than 0");
        }
    }
}