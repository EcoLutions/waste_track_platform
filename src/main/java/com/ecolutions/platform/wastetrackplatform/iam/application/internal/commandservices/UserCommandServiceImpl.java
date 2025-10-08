package com.ecolutions.platform.wastetrackplatform.iam.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.hashing.HashingService;
import com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.tokens.TokenService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignInCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignUpCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.Username;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if (userRepository.existsByUsername(new Username(command.username())))
            throw new RuntimeException("Username already exists");
        var roles = command.roles().stream().map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new RuntimeException("Role not found"))).toList();
        var user = new User(command.username(), hashingService.encode(command.password()), roles);
        userRepository.save(user);
        return userRepository.findByUsername(new Username(command.username()));
    }
    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(new Username(command.username()));
        if (user.isEmpty()) throw new RuntimeException("User not found");
        var existingUser = user.get();
        if(!hashingService.matches(command.password(), existingUser.getPassword().value())) throw new RuntimeException("Invalid password");
        var token = tokenService.generateToken(existingUser.getUsername().value());
        return Optional.of(ImmutablePair.of(existingUser, token));
    }
}