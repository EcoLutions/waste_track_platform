package com.ecolutions.platform.wastetrackplatform.iam.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.CreateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.DeleteUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.UpdateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.AccountStatus;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.HashedPassword;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.Username;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.command.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> handle(CreateUserCommand command) {
        try {
            if (userRepository.existsByUsername(new Username(command.username()))) {
                throw new IllegalArgumentException("Username already exists");
            }

            if (userRepository.existsByEmail(new EmailAddress(command.email()))) {
                throw new IllegalArgumentException("Email already exists");
            }

            var username = new Username(command.username());
            var email = new EmailAddress(command.email());
            var hashedPassword = new HashedPassword(hashPassword(command.password()));

            var newUser = new User(username, email, hashedPassword);

            var savedUser = userRepository.save(newUser);
            return Optional.of(savedUser);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> handle(UpdateUserCommand command) {
        try {
            var existingUser = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + command.userId() + " not found."));

            if (!existingUser.getUsername().value().equals(command.username()) &&
                userRepository.existsByUsername(new Username(command.username()))) {
                throw new IllegalArgumentException("Username already exists");
            }

            if (!existingUser.getEmail().value().equals(command.email()) &&
                userRepository.existsByEmail(new EmailAddress(command.email()))) {
                throw new IllegalArgumentException("Email already exists");
            }

            existingUser.setUsername(new Username(command.username()));
            existingUser.setEmail(new EmailAddress(command.email()));
            existingUser.setAccountStatus(AccountStatus.valueOf(command.accountStatus()));

            var updatedUser = userRepository.save(existingUser);
            return Optional.of(updatedUser);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update user: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteUserCommand command) {
        try {
            var existingUser = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + command.userId() + " not found."));

            userRepository.delete(existingUser);
            return true;

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete user: " + e.getMessage(), e);
        }
    }

    private String hashPassword(String password) {
        // TODO: Implement actual password hashing using bcrypt or similar
        // For now, return a placeholder hash
        return "hashed_" + password;
    }
}