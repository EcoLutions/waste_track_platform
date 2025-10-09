package com.ecolutions.platform.wastetrackplatform.shared.domain.services.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String uploadFile(MultipartFile file, String folder) throws IOException;
    String getFileUrl(String filePath) throws IOException;
    boolean deleteFile(String filePath);
    boolean fileExists(String filePath);
}