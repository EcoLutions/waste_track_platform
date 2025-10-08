package com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class User extends AuditableAbstractAggregateRoot<User> {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "username", nullable = false, unique = true))
    private Username username;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private EmailAddress email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "hashed_password", nullable = false))
    private HashedPassword hashedPassword;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    private Set<RoleId> roles;

    @NotNull
    private Integer failedLoginAttempts;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordChangedAt;

    private String activationToken;

    private LocalDateTime activationTokenExpiresAt;

    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    private static final int ACTIVATION_TOKEN_EXPIRY_HOURS = 24;

    public User() {
        super();
        this.accountStatus = AccountStatus.PENDING_ACTIVATION;
        this.failedLoginAttempts = 0;
        this.roles = new HashSet<>();
    }

    public User(Username username, EmailAddress email, HashedPassword hashedPassword) {
        this();
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.accountStatus = AccountStatus.PENDING_ACTIVATION;
        this.passwordChangedAt = LocalDateTime.now();
    }

    public AuthenticationResult authenticate(String rawPassword) {
        if (accountStatus == AccountStatus.LOCKED) {
            return AuthenticationResult.accountLocked();
        }

        if (accountStatus == AccountStatus.DISABLED) {
            return AuthenticationResult.accountDisabled();
        }

        if (accountStatus == AccountStatus.PENDING_ACTIVATION) {
            return AuthenticationResult.pendingActivation();
        }

        // TODO: Implement password verification logic
        boolean passwordMatches = verifyPassword(rawPassword);

        if (passwordMatches) {
            recordSuccessfulLogin();
            return AuthenticationResult.success(this.getId());
        } else {
            recordFailedLogin();
            return AuthenticationResult.failure("Invalid credentials");
        }
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!verifyPassword(oldPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters long");
        }

        // TODO: Hash the new password
        this.hashedPassword = new HashedPassword(hashPassword(newPassword));
        this.passwordChangedAt = LocalDateTime.now();
    }

    public void resetPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters long");
        }

        // TODO: Hash the new password
        this.hashedPassword = new HashedPassword(hashPassword(newPassword));
        this.passwordChangedAt = LocalDateTime.now();
        this.failedLoginAttempts = 0;
    }

    public void assignRole(RoleId roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("RoleId cannot be null");
        }
        this.roles.add(roleId);
    }

    public void removeRole(RoleId roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("RoleId cannot be null");
        }
        this.roles.remove(roleId);
    }

    public boolean hasPermission(Permission permission) {
        // TODO: Implement permission checking logic based on user's roles
        // For now, return false as placeholder
        return false;
    }

    public void lockAccount() {
        this.accountStatus = AccountStatus.LOCKED;
    }

    public void unlockAccount() {
        if (this.accountStatus == AccountStatus.LOCKED) {
            this.accountStatus = AccountStatus.ACTIVE;
            this.failedLoginAttempts = 0;
        }
    }

    public void recordSuccessfulLogin() {
        this.lastLoginAt = LocalDateTime.now();
        this.failedLoginAttempts = 0;
    }

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
            this.accountStatus = AccountStatus.LOCKED;
        }
    }

    public String generateActivationToken() {
        this.activationToken = java.util.UUID.randomUUID().toString();
        this.activationTokenExpiresAt = LocalDateTime.now().plusHours(ACTIVATION_TOKEN_EXPIRY_HOURS);
        return this.activationToken;
    }

    public void activateAccount(String token) {
        if (this.accountStatus != AccountStatus.PENDING_ACTIVATION) {
            throw new IllegalArgumentException("Account is not pending activation");
        }

        if (token == null || !token.equals(this.activationToken)) {
            throw new IllegalArgumentException("Invalid activation token");
        }

        if (this.activationTokenExpiresAt != null && LocalDateTime.now().isAfter(this.activationTokenExpiresAt)) {
            throw new IllegalArgumentException("Activation token has expired");
        }

        this.accountStatus = AccountStatus.ACTIVE;
        this.activationToken = null;
        this.activationTokenExpiresAt = null;
    }

    public boolean isAccountLocked() {
        return this.accountStatus == AccountStatus.LOCKED;
    }

    public boolean requiresPasswordChange() {
        // TODO: Implement password change policy (e.g., every 90 days)
        // For now, return false as placeholder
        return false;
    }

    private boolean verifyPassword(String rawPassword) {
        // TODO: Implement actual password verification using bcrypt or similar
        // For now, return true as placeholder
        return rawPassword != null && !rawPassword.isEmpty();
    }

    private String hashPassword(String password) {
        // TODO: Implement actual password hashing using bcrypt or similar
        // For now, return a placeholder hash
        return "hashed_" + password;
    }
}
