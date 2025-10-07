package com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

public record UploadPhotoCommand(
    MultipartFile file
) {
    public UploadPhotoCommand {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }
    }
}