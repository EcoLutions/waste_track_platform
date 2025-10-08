package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetCurrentUserQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserQueryService;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SignInResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SignUpResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.AuthenticatedUserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.UserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse.AuthenticatedUserResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse.UserResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand.SignInCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromresourcetocommand.SignUpCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.swagger.AuthenticationController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @Override
    public ResponseEntity<UserResource> signUp(SignUpResource resource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(signUpCommand);
        if (user.isEmpty()) return ResponseEntity.badRequest().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        URI location = URI.create("/api/v1/users/" + userResource.id());
        return ResponseEntity.created(location).body(userResource);
    }

    @Override
    public ResponseEntity<AuthenticatedUserResource> signIn(SignInResource resource) {
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var authenticatedUserResult = userCommandService.handle(signInCommand);
        if (authenticatedUserResult.isEmpty()) return ResponseEntity.notFound().build();
        var authenticatedUser = authenticatedUserResult.get();
        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(authenticatedUser.left, authenticatedUser.right);
        return ResponseEntity.ok(authenticatedUserResource);
    }

    @Override
    public ResponseEntity<UserResource> getCurrentUser() {
        var query = new GetCurrentUserQuery();
        var user = userQueryService.handle(query);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }
}
