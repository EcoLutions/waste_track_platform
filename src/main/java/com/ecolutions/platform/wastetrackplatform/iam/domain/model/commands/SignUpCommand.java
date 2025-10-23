package com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;

import java.util.List;

public record SignUpCommand(String email, String password, List<Role> roles) {
}
