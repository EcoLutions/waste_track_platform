package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.ResetPasswordResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SetInitialPasswordResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SignInResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.SignUpResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.AuthenticatedUserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.ResetCompletedPasswordResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.SetCompletedInitialPasswordResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.UserResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Available Authentication Endpoints")
public interface AuthenticationController {
    @SecurityRequirements()
    @PostMapping("/sign-up")
    @Operation(summary = "Sign up a new user", description = "Sign up a new user with the provided email, password, and roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.")
    })
    ResponseEntity<UserResource> signUp(@RequestBody SignUpResource resource);

    @SecurityRequirements()
    @PostMapping("/sign-in")
    @Operation(summary = "Sign in a user", description = "Sign in a user with the provided email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User signed in successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource resource);

    @SecurityRequirements()
    @PostMapping("/set-initial-password")
    @Operation(summary = "Set initial password", description = "Set the initial password for a newly created user using the activation token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Initial password set successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid activation token or password."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    ResponseEntity<SetCompletedInitialPasswordResource> setInitialPassword(@RequestBody SetInitialPasswordResource resource);

    @SecurityRequirements()
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Initiate the forgot password process for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forgot password process initiated successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    ResponseEntity<Void> forgotPassword(@RequestParam String email);

    @SecurityRequirements()
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset the password for a user using the reset token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid reset token or password."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    ResponseEntity<ResetCompletedPasswordResource> resetPassword(@RequestBody ResetPasswordResource resource);

    @SecurityRequirements()
    @PostMapping("/resend-activation-token")
    @Operation(summary = "Resend activation token", description = "Resend the activation token to a user who has not yet activated their account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activation token resent successfully."),
            @ApiResponse(responseCode = "400", description = "Account is not pending activation."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    ResponseEntity<Void> resendActivationToken(@RequestParam String userId);

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get the currently authenticated user information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current user retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "User not authenticated."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    ResponseEntity<UserResource> getCurrentUser();
}
