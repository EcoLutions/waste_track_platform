package com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.AccountStatus;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.Password;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private EmailAddress email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password", nullable = false))
    private Password password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @NotNull
    private Integer failedLoginAttempts;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordChangedAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
    }

    public User(String email, String password) {
        this();
        this.email = new EmailAddress(email);
        this.password = new Password(password);
    }

    public User(String email, String password, List<Role> roles) {
        this();
        this.email = new EmailAddress(email);
        this.password = new Password(password);
        this.addRoles(roles);
    }

    public User addRole(Role role) {
        this.roles.add(role);
        return this;
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

    public void activateAccount(String token) {
        if (this.accountStatus != AccountStatus.PENDING_ACTIVATION) {
            throw new IllegalArgumentException("Account is not pending activation");
        }
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public boolean isAccountLocked() {
        return this.accountStatus == AccountStatus.LOCKED;
    }

    public boolean requiresPasswordChange() {
        // TODO: Implement password change policy (e.g., every 90 days)
        // For now, return false as placeholder
        return false;
    }
}
