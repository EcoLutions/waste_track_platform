package com.ecolutions.platform.wastetrackplatform.profile.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.UploadPhotoCommand;

import java.util.Optional;

public interface PhotoCommandService {
    Optional<String> handle(UploadPhotoCommand command);
}