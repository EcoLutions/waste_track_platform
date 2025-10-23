package com.ecolutions.platform.wastetrackplatform.iam.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.hashing.HashingService;
import com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.tokens.TokenService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.CreateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SeedSuperAdminCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignInCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignUpCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.AccountStatus;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.Roles;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(new EmailAddress(command.email())))
            throw new IllegalArgumentException("Username already exists");
        boolean onlyCitizen = command.roles().size() == 1 &&
                command.roles().getFirst().getName() == Roles.ROLE_CITIZEN;

        if (!onlyCitizen)
            throw new IllegalArgumentException("Public registration only allows CITIZEN role");

        var roles = command.roles().stream().map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new RuntimeException("Role not found"))).toList();
        var user = new User(command.email(), hashingService.encode(command.password()), roles);
        userRepository.save(user);
        return userRepository.findByEmail(new EmailAddress(command.email()));
    }
    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByEmail(new EmailAddress(command.email()));
        if (user.isEmpty()) throw new IllegalArgumentException("Email not found");
        var existingUser = user.get();

        if (existingUser.isAccountLocked()) {
            throw new IllegalArgumentException("Account is locked due to too many failed login attempts");
        }

        if (existingUser.getAccountStatus() == AccountStatus.PENDING_ACTIVATION) {
            throw new IllegalArgumentException("Account not activated. Please check your email for activation link");
        }

        if(!hashingService.matches(command.password(), existingUser.getPassword().value())) {
            existingUser.recordFailedLogin();
            userRepository.save(existingUser);
            throw new IllegalArgumentException("Invalid password");
        }

        existingUser.recordSuccessfulLogin();
        userRepository.save(existingUser);

        var token = tokenService.generateToken(existingUser.getEmail().value());
        return Optional.of(ImmutablePair.of(existingUser, token));
    }

    @Override
    public Optional<User> handle(CreateUserCommand command) {
        if (userRepository.existsByEmail(new EmailAddress(command.email())))
            throw new IllegalArgumentException("Email already exists");

        validateRolePermissions(command.roles());

        var roles = command.roles().stream().map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new RuntimeException("Role not found"))).toList();
        User newUser = new User(command);
        newUser.addRoles(roles);
        try {
            userRepository.save(newUser);
            return Optional.of(newUser);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create user");
        }
    }

    @Override
    public Optional<User> handle(SeedSuperAdminCommand command) {
        if (userRepository.existsByEmail(new EmailAddress(command.email())))
            return userRepository.findByEmail(new EmailAddress(command.email()));

        Role systemAdminRole = roleRepository.findByName(Roles.ROLE_SYSTEM_ADMINISTRATOR)
                .orElseThrow(() -> new RuntimeException("SYSTEM_ADMINISTRATOR role not found"));

        User superAdmin = new User(command.email(), hashingService.encode(command.password()));
        superAdmin.addRole(systemAdminRole);

        userRepository.save(superAdmin);
        return Optional.of(superAdmin);
    }

    private void validateRolePermissions(List<Role> requestedRoles) {
        boolean onlyCitizen = requestedRoles.size() == 1 && requestedRoles.getFirst().getName() == Roles.ROLE_CITIZEN;
        if (onlyCitizen) throw new IllegalArgumentException("Insufficient permissions to assign role Citizen");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasMunicipalAdminRole = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Roles.ROLE_MUNICIPAL_ADMINISTRATOR.name()));

        boolean hasSystemAdminRole = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Roles.ROLE_SYSTEM_ADMINISTRATOR.name()));

        List<Roles> requestedRoleNames = requestedRoles.stream()
                .map(Role::getName)
                .toList();

        if (hasSystemAdminRole) {
            boolean canCreateAll = requestedRoleNames.stream()
                    .allMatch(role -> role == Roles.ROLE_SYSTEM_ADMINISTRATOR || role == Roles.ROLE_MUNICIPAL_ADMINISTRATOR);

            if (canCreateAll) return;
        }

        if (hasMunicipalAdminRole) {
            boolean canCreateAll = requestedRoleNames.stream()
                    .allMatch(role -> role == Roles.ROLE_MUNICIPAL_ADMINISTRATOR || role == Roles.ROLE_DRIVER);

            if (canCreateAll) return;
        }

        String rolesRequested = requestedRoleNames.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        throw new IllegalArgumentException("Insufficient permissions to create users with roles: " + rolesRequested);
    }
}