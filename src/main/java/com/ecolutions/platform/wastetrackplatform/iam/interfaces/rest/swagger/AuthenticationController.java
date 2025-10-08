package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SignInResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SignUpResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.AuthenticatedUserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.UserResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Available Authentication Endpoints")
public interface AuthenticationController {
    @SecurityRequirements()
    @PostMapping("/sign-up")
    @Operation(summary = "Sign up a new user", description = "Sign up a new user with the provided username, password, and roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.")
    })
    ResponseEntity<UserResource> signUp(@RequestBody SignUpResource resource);

    @SecurityRequirements()
    @PostMapping("/sign-in")
    @Operation(summary = "Sign in a user", description = "Sign in a user with the provided username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User signed in successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource resource);

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get the currently authenticated user information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current user retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "User not authenticated."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    ResponseEntity<UserResource> getCurrentUser();
}
