package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.UploadPhotoCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.services.command.PhotoCommandService;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response.PhotoResource;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.PhotoResourceFromStringAssembler;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.swagger.PhotoController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PhotoControllerImpl implements PhotoController {
    private final PhotoCommandService photoCommandService;

    @Override
    public ResponseEntity<PhotoResource> uploadPhoto(MultipartFile file) {
        var command = new UploadPhotoCommand(file);
        var uploadedPhotoPath = photoCommandService.handle(command);
        if (uploadedPhotoPath.isEmpty()) return ResponseEntity.badRequest().build();
        var photoResource = PhotoResourceFromStringAssembler.toResourceFromString(uploadedPhotoPath.get());
        return ResponseEntity.ok(photoResource);
    }
}