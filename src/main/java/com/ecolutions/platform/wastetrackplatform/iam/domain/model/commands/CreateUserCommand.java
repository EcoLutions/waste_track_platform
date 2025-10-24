package com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;

import java.util.List;

public record CreateUserCommand(String email, List<Role> roles, String district) {
    public CreateUserCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
}