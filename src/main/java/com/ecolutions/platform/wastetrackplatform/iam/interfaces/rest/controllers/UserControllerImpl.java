package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.DeleteUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetAllUsersQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetUserByIdQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.command.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.queries.UserQueryService;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.CreateUserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.UpdateUserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.UserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand.CreateUserCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse.UserResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand.UpdateUserCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.swagger.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @Override
    public ResponseEntity<UserResource> createUser(CreateUserResource resource) {
        var command = CreateUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdUser = userCommandService.handle(command);
        if (createdUser.isEmpty()) return ResponseEntity.badRequest().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(createdUser.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResource.id())
                .toUri();
        return ResponseEntity.created(location).body(userResource);
    }

    @Override
    public ResponseEntity<UserResource> getUserById(String id) {
        var query = new GetUserByIdQuery(id);
        var user = userQueryService.handle(query);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.status(HttpStatus.OK).body(userResource);
    }

    @Override
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var query = new GetAllUsersQuery();
        var users = userQueryService.handle(query);
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userResources);
    }

    @Override
    public ResponseEntity<UserResource> updateUser(String id, UpdateUserResource resource) {
        var command = UpdateUserCommandFromResourceAssembler.toCommandFromResource(resource, id);
        var updatedUser = userCommandService.handle(command);
        if (updatedUser.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(updatedUser.get());
        return ResponseEntity.status(HttpStatus.OK).body(userResource);
    }

    @Override
    public ResponseEntity<Void> deleteUser(String id) {
        var command = new DeleteUserCommand(id);
        var deleted = userCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}