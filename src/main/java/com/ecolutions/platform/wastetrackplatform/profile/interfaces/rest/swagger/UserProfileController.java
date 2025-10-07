package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request.CreateUserProfileResource;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.request.UpdateUserProfileResource;
import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response.UserProfileResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/user-profiles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "UserProfile", description = "UserProfile Management Endpoints")
public interface UserProfileController {

    @PostMapping()
    @Operation(summary = "Create a new user profile", description = "Creates a new user profile in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "UserProfile created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<UserProfileResource> createUserProfile(@RequestBody CreateUserProfileResource resource) throws IOException ;

    @GetMapping("/{id}")
    @Operation(summary = "Get user profile by ID", description = "Retrieves a user profile by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UserProfile retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "UserProfile not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<UserProfileResource> getUserProfileById(@PathVariable String id) throws IOException;

    @GetMapping()
    @Operation(summary = "Get all user profiles", description = "Retrieves all user profiles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UserProfiles retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<UserProfileResource>> getAllUserProfiles();

    @PutMapping("/{id}")
    @Operation(summary = "Update user profile", description = "Updates an existing user profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UserProfile updated successfully."),
            @ApiResponse(responseCode = "404", description = "UserProfile not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<UserProfileResource> updateUserProfile(@PathVariable String id, @RequestBody UpdateUserProfileResource resource) throws IOException;

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user profile", description = "Deletes a user profile by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "UserProfile deleted successfully."),
            @ApiResponse(responseCode = "404", description = "UserProfile not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteUserProfile(@PathVariable String id);
}