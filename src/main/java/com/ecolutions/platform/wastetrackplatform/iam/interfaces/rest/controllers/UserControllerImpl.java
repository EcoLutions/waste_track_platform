package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetAllUsersQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetUserByIdQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserQueryService;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.CreateUserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.UserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse.UserResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand.CreateUserCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.swagger.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Override
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var getAllUsersQuery = new GetAllUsersQuery();
        var users = userQueryService.handle(getAllUsersQuery);
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(userResources);
    }

    @Override
    public ResponseEntity<UserResource> getUserById(String userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

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
}
