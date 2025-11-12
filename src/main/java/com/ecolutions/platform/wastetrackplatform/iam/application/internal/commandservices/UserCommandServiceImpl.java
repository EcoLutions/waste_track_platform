package com.ecolutions.platform.wastetrackplatform.iam.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.hashing.HashingService;
import com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.tokens.TokenService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.*;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Roles;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, RoleRepository roleRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(new EmailAddress(command.email())))
            throw new IllegalArgumentException("Username already exists");

        Role citizenRole = roleRepository.findByName(Roles.ROLE_CITIZEN)
                .orElseThrow(() -> new IllegalStateException("CITIZEN role not found"));

        User user = new User(command,
                hashingService.encode(command.password()));

        user.addRoles(List.of(citizenRole));

        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
    }

    @Override
    @Transactional
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        User user = userRepository.findByEmail(new EmailAddress(command.email()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (user.isAccountPendingActivation()) {
            throw new IllegalArgumentException("User account is pending activation");
        }

        if (user.isAccountLocked()) {
            throw new IllegalArgumentException("Account is locked due to too many failed login attempts");
        }

        if(!hashingService.matches(command.password(), user.getPassword().value())) {
            user.recordFailedLogin();
            userRepository.save(user);
            throw new IllegalArgumentException("Invalid password");
        }

        user.recordSuccessfulLogin();
        userRepository.save(user);

        String token = tokenService.generateToken(user.getEmail().value());
        return Optional.of(ImmutablePair.of(user, token));
    }

    @Override
    @Transactional
    public Optional<User> handle(CreateUserCommand command) {
        if (userRepository.existsByEmail(new EmailAddress(command.email())))
            throw new IllegalArgumentException("Email already exists");
        var roles = command.roles().stream().map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new IllegalArgumentException("Role not found"))).toList();
        User newUser = new User(command);
        newUser.addRoles(roles);
        var savedUser = userRepository.save(newUser);
        var activationToken = tokenService.generateActivationToken(newUser.getEmail().value());
        var event = savedUser.publishUserCreatedEvent(activationToken);
        eventPublisher.publishEvent(event);
        return Optional.of(savedUser);
    }

    @Override
    @Transactional
    public Optional<User> handle(SeedSuperAdminCommand command) {
        Optional<User> existingUser = userRepository.findByEmail(new EmailAddress(command.email()));
        if (existingUser.isPresent()) return existingUser;

        Role systemAdminRole = roleRepository.findByName(Roles.ROLE_SYSTEM_ADMINISTRATOR)
                .orElseThrow(() -> new RuntimeException("SYSTEM_ADMINISTRATOR role not found"));

        User superAdmin = new User(command, hashingService.encode(command.password()));
        superAdmin.addRole(systemAdminRole);

        userRepository.save(superAdmin);

        return Optional.of(superAdmin);
    }

    @Override
    @Transactional
    public void handle(SetInitialPasswordCommand command) {
        if (!tokenService.validateToken(command.activationToken())) {
            throw new IllegalArgumentException("Invalid or expired activation token");
        }

        String tokenPurpose = tokenService.getTokenPurpose(command.activationToken());
        if (!"activation".equals(tokenPurpose)) {
            throw new IllegalArgumentException("Invalid token type for activation");
        }

        String userEmail = tokenService.getEmailFromToken(command.activationToken());
        User user = userRepository.findByEmail(new EmailAddress(userEmail))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.isAccountPendingActivation())
            throw new IllegalArgumentException("Account is not pending activation");

        user.activateAccount();
        user.changePassword(hashingService.encode(command.password()));
        userRepository.save(user);
    }

    @Override
    public void handle(RequestPasswordResetCommand command) {
        User user = userRepository.findByEmail(new EmailAddress(command.email()))
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + command.email()));
        var resetToken = tokenService.generatePasswordResetToken(user.getEmail().value());
        var event = user.publishPasswordResetRequestedEvent(resetToken);
        eventPublisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void handle(ResetPasswordCommand command) {
        if (!tokenService.validateToken(command.token())) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        String tokenPurpose = tokenService.getTokenPurpose(command.token());
        if (!"password_reset".equals(tokenPurpose)) {
            throw new IllegalArgumentException("Invalid token type for password reset");
        }

        String userEmail = tokenService.getEmailFromToken(command.token());
        User user = userRepository.findByEmail(new EmailAddress(userEmail))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isAccountPendingActivation())
            throw new IllegalArgumentException("Account is pending activation");

        user.changePassword(hashingService.encode(command.password()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void handle(ResendActivationTokenCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + command.userId()));

        if (!user.isAccountPendingActivation()) {
            throw new IllegalArgumentException("Account is not pending activation");
        }

        var activationToken = tokenService.generateActivationToken(user.getEmail().value());
        var event = user.publishActivationTokenResentEvent(activationToken);
        eventPublisher.publishEvent(event);
    }
}