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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserProfileControllerImpl implements UserProfileController {
    private final UserProfileCommandService userProfileCommandService;
    private final UserProfileQueryService userProfileQueryService;

    @Override
    public ResponseEntity<UserProfileResource> createUserProfile(CreateUserProfileResource resource) {
        var command = CreateUserProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdUserProfile = userProfileCommandService.handle(command);
        if (createdUserProfile.isEmpty()) return ResponseEntity.badRequest().build();
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(createdUserProfile.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userProfileResource.id())
                .toUri();
        return ResponseEntity.created(location).body(userProfileResource);
    }

    @Override
    public ResponseEntity<UserProfileResource> getUserProfileById(String id) {
        var query = new GetUserProfileByIdQuery(id);
        var userProfile = userProfileQueryService.handle(query);
        if (userProfile.isEmpty()) return ResponseEntity.notFound().build();
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(userProfile.get());
        return ResponseEntity.status(HttpStatus.OK).body(userProfileResource);
    }

    @Override
    public ResponseEntity<List<UserProfileResource>> getAllUserProfiles() {
        var query = new GetAllUserProfilesQuery();
        var userProfiles = userProfileQueryService.handle(query);
        var userProfileResources = userProfiles.stream()
                .map(UserProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userProfileResources);
    }

    @Override
    public ResponseEntity<UserProfileResource> updateUserProfile(String id, UpdateUserProfileResource resource) {
        var command = UpdateUserProfileCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedUserProfile = userProfileCommandService.handle(command);
        if (updatedUserProfile.isEmpty()) return ResponseEntity.notFound().build();
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(updatedUserProfile.get());
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