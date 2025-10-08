package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.CreateUserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.request.UpdateUserResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.UserResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "User", description = "User Management Endpoints")
public interface UserController {

    @PostMapping()
    @Operation(summary = "Create a new user", description = "Creates a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<UserResource> createUser(@RequestBody CreateUserResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<UserResource> getUserById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all users", description = "Retrieves all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<UserResource>> getAllUsers();

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<UserResource> updateUser(@PathVariable String id, @RequestBody UpdateUserResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteUser(@PathVariable String id);
}