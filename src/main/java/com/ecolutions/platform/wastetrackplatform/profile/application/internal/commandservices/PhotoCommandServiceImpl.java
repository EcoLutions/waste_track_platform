package com.ecolutions.platform.wastetrackplatform.profile.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.UploadPhotoCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.services.command.PhotoCommandService;
import com.ecolutions.platform.wastetrackplatform.shared.domain.services.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoCommandServiceImpl implements PhotoCommandService {
    private final StorageService storageService;

    @Override
    public Optional<String> handle(UploadPhotoCommand command) {
        try {
            String filePath = storageService.uploadFile(command.file(), "user-photos");
            return Optional.of(filePath);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to upload photo: " + e.getMessage());
        }
    }
}