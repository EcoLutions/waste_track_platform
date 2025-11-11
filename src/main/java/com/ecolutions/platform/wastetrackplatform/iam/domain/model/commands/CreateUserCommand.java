package com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;

import java.util.List;

public record CreateUserCommand(String email, String username, List<Role> roles, String districtId) {
    public CreateUserCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
    }
}