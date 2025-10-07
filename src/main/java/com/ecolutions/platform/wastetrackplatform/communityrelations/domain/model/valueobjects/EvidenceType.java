package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects;

public enum EvidenceType {
    PHOTO,
    VIDEO,
    DOCUMENT;

    public static EvidenceType fromMimeType(String mimeType) {
        if (mimeType == null) {
            throw new IllegalArgumentException("MIME type cannot be null");
        }

        if (mimeType.startsWith("image/")) {
            return PHOTO;
        } else if (mimeType.startsWith("video/")) {
            return VIDEO;
        } else {
            return DOCUMENT;
        }
    }
}
