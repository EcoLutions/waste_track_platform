package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.DeleteUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.queries.GetAllUserProfilesQuery;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.queries.GetUserProfileByIdQuery;
import com.ecolutions.platform.wastetrackplatform.profile.domain.services.command.UserProfileCommandService;
import com.ecolutions.platform.wastetrackplatform.profile.domain.services.queries.UserProfileQueryService;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request.CreateUserProfileResource;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request.UpdateUserProfileResource;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response.UserProfileResource;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.fromentitytoresponse.UserProfileResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.fromresourcetocommand.CreateUserProfileCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers.fromresourcetocommand.UpdateUserProfileCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.swagger.UserProfileController;
import com.ecolutions.platform.wastetrackplatform.shared.domain.services.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserProfileControllerImpl implements UserProfileController {
    private final UserProfileCommandService userProfileCommandService;
    private final UserProfileQueryService userProfileQueryService;
    private final StorageService storageService;

    @Override
    public ResponseEntity<UserProfileResource> createUserProfile(CreateUserProfileResource resource) throws IOException  {
        var command = CreateUserProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdUserProfile = userProfileCommandService.handle(command);
        if (createdUserProfile.isEmpty()) return ResponseEntity.badRequest().build();
        String freshPhotoUrl = storageService.getFileUrl(createdUserProfile.get().getPhoto().filePath());
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(createdUserProfile.get(), freshPhotoUrl);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userProfileResource.id())
                .toUri();
        return ResponseEntity.created(location).body(userProfileResource);
    }

    @Override
    public ResponseEntity<UserProfileResource> getUserProfileById(String id) throws IOException {
        var query = new GetUserProfileByIdQuery(id);
        var userProfile = userProfileQueryService.handle(query);
        if (userProfile.isEmpty()) return ResponseEntity.notFound().build();
        String freshPhotoUrl = storageService.getFileUrl(userProfile.get().getPhoto().filePath());
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(userProfile.get(), freshPhotoUrl);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileResource);
    }

    @Override
    public ResponseEntity<List<UserProfileResource>> getAllUserProfiles() {
        var query = new GetAllUserProfilesQuery();
        var userProfiles = userProfileQueryService.handle(query);
        var userProfileResources = userProfiles.stream()
                .map(userProfile -> {
                    try {
                        String freshPhotoUrl = storageService.getFileUrl(userProfile.getPhoto().filePath());
                        return UserProfileResourceFromEntityAssembler.toResourceFromEntity(userProfile, freshPhotoUrl);
                    } catch (IOException e) {
                        log.warn("Could not generate photo URL for user profile {}: {}", userProfile.getId(), e.getMessage());
                        return UserProfileResourceFromEntityAssembler.toResourceFromEntity(userProfile, null);
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(userProfileResources);
    }

    @Override
    public ResponseEntity<UserProfileResource> updateUserProfile(String id, UpdateUserProfileResource resource) throws IOException {
        var command = UpdateUserProfileCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedUserProfile = userProfileCommandService.handle(command);
        if (updatedUserProfile.isEmpty()) return ResponseEntity.notFound().build();
        String freshPhotoUrl = storageService.getFileUrl(updatedUserProfile.get().getPhoto().filePath());
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(updatedUserProfile.get(), freshPhotoUrl);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileResource);
    }

    @Override
    public ResponseEntity<Void> deleteUserProfile(String id) {
        var command = new DeleteUserProfileCommand(id);
        var deleted = userProfileCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}