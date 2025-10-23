package com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.CreateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.UserCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.AccountStatus;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.Password;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;


@Getter
@Setter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private EmailAddress email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private Password password;

    private Boolean isTemporaryPassword;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @NotNull
    private Integer failedLoginAttempts;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordChangedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;

    public User() {
        super();
        this.accountStatus = AccountStatus.PENDING_ACTIVATION;
        this.failedLoginAttempts = 0;
        this.roles = new HashSet<>();
        this.isTemporaryPassword = false;
    }

    public User(String email, String password) {
        this();
        this.email = new EmailAddress(email);
        this.password = new Password(password);
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public User(String email, String password, List<Role> roles) {
        this();
        this.email = new EmailAddress(email);
        this.password = new Password(password);
        this.addRoles(roles);
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public User(CreateUserCommand command) {
        this();
        this.email = new EmailAddress(command.email());
        this.password = new Password(this.generateTemporaryPassword());
        this.isTemporaryPassword = true;
        this.accountStatus = AccountStatus.PENDING_ACTIVATION;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addRoles(List<Role> roles) {
        var validatedRoles = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoles);
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

    public void activateAccount() {
        this.accountStatus = AccountStatus.ACTIVE;
        this.passwordChangedAt = LocalDateTime.now();
    }

    public boolean isAccountLocked() {
        return this.accountStatus == AccountStatus.LOCKED;
    }

    public boolean requiresPasswordChange() {
        // TODO: Implement password change policy (e.g., every 90 days)
        // For now, return false as placeholder
        return false;
    }

    private String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, 10)
                .map(i -> chars.charAt(random.nextInt(chars.length())))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public void invite() {
        var event = UserCreatedEvent.builder()
                .source(this)
                .email(this.email.value())
                .temporalPassword(this.password.value())
                .roles(this.roles.stream()
                        .map(role -> role.getName().toString())
                        .toList())
                .build();

        registerEvent(event);
    }
}
