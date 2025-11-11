package com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.CreateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SeedSuperAdminCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignUpCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.PasswordResetRequestedEvent;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.UserCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.AccountStatus;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.Password;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Roles;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Username;
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

    @NotNull
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "username"))
    private Username username;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private Password password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @NotNull
    private Integer failedLoginAttempts;

    @Transient
    private DistrictId districtId;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordChangedAt;

    @NotNull
    private Boolean hasProfile;

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
        this.hasProfile = false;
    }

    public User(SeedSuperAdminCommand command, String password) {
        this();
        this.email = new EmailAddress(command.email());
        this.username = new Username(command.username());
        this.password = new Password(password);
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public User(SignUpCommand command, String password) {
        this();
        this.username = new Username(command.username());
        this.email = new EmailAddress(command.email());
        this.password = new Password(password);
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public User(CreateUserCommand command) {
        this();
        this.username = new Username(command.username());
        this.email = new EmailAddress(command.email());
        this.accountStatus = AccountStatus.PENDING_ACTIVATION;
        this.districtId = DistrictId.of(command.districtId());
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addRoles(List<Role> roles) {
        var validatedRoles = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoles);

        this.setHasProfileBaseOnRoles();
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
        if (this.accountStatus == AccountStatus.PENDING_ACTIVATION) {
            this.accountStatus = AccountStatus.ACTIVE;
        }
    }

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
            this.accountStatus = AccountStatus.LOCKED;
        }
    }

    public void activateAccount() {
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public void changePassword(String newPassword) {
        this.password = new Password(newPassword);
        this.passwordChangedAt = LocalDateTime.now();
    }

    public boolean isAccountLocked() {
        return this.accountStatus == AccountStatus.LOCKED;
    }


    public boolean isAccountPendingActivation() {
        return this.accountStatus == AccountStatus.PENDING_ACTIVATION;
    }

    public UserCreatedEvent publishUserCreatedEvent(String activationToken) {
        return UserCreatedEvent.builder()
                .source(this)
                .userId(this.getId())
                .email(EmailAddress.toStringOrNull(this.email))
                .username(Username.toStringOrNull(this.username))
                .activationToken(activationToken)
                .roles(this.roles.stream()
                        .map(role -> role.getName().name())
                        .toList())
                .districtId(DistrictId.toStringOrNull(this.districtId))
                .build();
    }

    public PasswordResetRequestedEvent publishPasswordResetRequestedEvent(String resetToken) {
        return PasswordResetRequestedEvent.builder()
                .source(this)
                .userId(this.getId())
                .email(EmailAddress.toStringOrNull(this.email))
                .username(Username.toStringOrNull(this.username))
                .resetToken(resetToken)
                .build();
    }

    private void setHasProfileBaseOnRoles() {
        this.hasProfile = !this.roles.contains(new Role(Roles.ROLE_SYSTEM_ADMINISTRATOR));
    }
}
