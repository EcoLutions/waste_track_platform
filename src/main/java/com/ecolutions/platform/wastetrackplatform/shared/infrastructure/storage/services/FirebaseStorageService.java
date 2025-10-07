package com.ecolutions.platform.wastetrackplatform.shared.infrastructure.storage.services;

import com.ecolutions.platform.wastetrackplatform.shared.domain.services.storage.StorageService;
import com.ecolutions.platform.wastetrackplatform.shared.infrastructure.storage.configuration.FirebaseProperties;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseStorageService implements StorageService {

    private final Storage storage;
    private final FirebaseProperties firebaseProperties;

    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        validateFile(file);

        try {
            String filePath = buildFilePath(file.getOriginalFilename(), folder);

            BlobInfo blobInfo = BlobInfo.newBuilder(getBucketName(), filePath)
                    .setContentType(file.getContentType())
                    .setContentDisposition("inline; filename=\"" + file.getOriginalFilename() + "\"")
                    .build();

            storage.create(blobInfo, file.getBytes());

            log.info("File uploaded successfully: {}", filePath);
            return filePath;

        } catch (StorageException e) {
            log.error("Storage error uploading file: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file to storage", e);
        } catch (Exception e) {
            log.error("Unexpected error uploading file: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file", e);
        }
    }

    @Override
    public String getFileUrl(String filePath) throws IOException {
        validateFilePath(filePath);

        try {
            Blob blob = getBlob(filePath);

            if (blob == null || !blob.exists()) {
                throw new IOException("File not found: " + filePath);
            }

            URL signedUrl = blob.signUrl(
                    getUrlExpirationHours(),
                    TimeUnit.HOURS
            );

            log.debug("Generated URL for file: {}", filePath);
            return signedUrl.toString();

        } catch (StorageException e) {
            log.error("Storage error generating URL for file {}: {}", filePath, e.getMessage());
            throw new IOException("Failed to generate file URL", e);
        }
    }

    @Override
    public boolean deleteFile(String filePath) {
        validateFilePath(filePath);

        try {
            Blob blob = getBlob(filePath);

            if (blob == null || !blob.exists()) {
                log.warn("File not found for deletion: {}", filePath);
                return false;
            }

            boolean deleted = blob.delete();

            if (deleted) {
                log.info("File deleted successfully: {}", filePath);
            } else {
                log.warn("Failed to delete file: {}", filePath);
            }

            return deleted;

        } catch (StorageException e) {
            log.error("Storage error deleting file {}: {}", filePath, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean fileExists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        try {
            Blob blob = getBlob(filePath);
            return blob != null && blob.exists();

        } catch (StorageException e) {
            log.error("Error checking file existence {}: {}", filePath, e.getMessage());
            return false;
        }
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("File cannot be null or empty");
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
            throw new IOException("File name cannot be null or empty");
        }
    }

    private void validateFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
    }

    private String buildFilePath(String originalFileName, String folder) {
        String fileExtension = extractFileExtension(originalFileName);
        String uniqueFileName = UUID.randomUUID() + fileExtension;

        String sanitizedFolder = folder != null ? folder.toLowerCase().trim() : "general";
        String basePath = firebaseProperties.getStorage().getBasePath();

        return String.format("%s/%s/%s", basePath, sanitizedFolder, uniqueFileName);
    }

    private String extractFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private Blob getBlob(String filePath) {
        return storage.get(BlobId.of(getBucketName(), filePath));
    }

    private String getBucketName() {
        return firebaseProperties.getStorage().getBucket();
    }

    private Integer getUrlExpirationHours() {
        return firebaseProperties.getStorage().getUrlExpirationHours();
    }
}