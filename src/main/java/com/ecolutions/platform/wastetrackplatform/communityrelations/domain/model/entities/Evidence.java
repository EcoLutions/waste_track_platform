package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.entities;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.EvidenceType;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Evidence extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EvidenceType type;

    @NotBlank
    private String filePath;

    @NotBlank
    private String originalFileName;

    private String description;

    @NotNull
    private Long fileSize;

    @NotBlank
    private String mimeType;

    private String thumbnailUrl;

    public Evidence() {
        super();
    }

    public Evidence(EvidenceType type, String filePath, String originalFileName,
                    String description, Long fileSize, String mimeType) {
        this();
        this.type = type;
        this.filePath = filePath;
        this.originalFileName = originalFileName;
        this.description = description;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
    }

    public boolean isImage() {
        return this.type == EvidenceType.PHOTO;
    }

    public boolean isVideo() {
        return this.type == EvidenceType.VIDEO;
    }

    public boolean isLargeFile() {
        return this.fileSize > 10 * 1024 * 1024; // 10MB
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void generateThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}