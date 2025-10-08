package com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.Permission;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.RoleName;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.RoleType;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role extends AuditableAbstractAggregateRoot<Role> {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false, unique = true))
    private RoleName name;

    @Column(name = "description", nullable = false)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission")
    private Set<Permission> permissions;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Role() {
        super();
        this.permissions = new HashSet<>();
        this.isActive = true;
    }

    public Role(RoleName name, String description, RoleType roleType) {
        this();
        this.name = name;
        this.description = description;
        this.roleType = roleType;
    }

    public void addPermission(Permission permission) {
        if (permission == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        }

        if (roleType == RoleType.SYSTEM && canBeModified()) {
            throw new IllegalArgumentException("System roles cannot be modified");
        }

        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        if (permission == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        }

        if (roleType == RoleType.SYSTEM && canBeModified()) {
            throw new IllegalArgumentException("System roles cannot be modified");
        }

        this.permissions.remove(permission);
    }

    public boolean hasPermission(Permission permission) {
        if (permission == null) {
            return false;
        }

        if (!this.isActive) {
            return false;
        }

        return this.permissions.contains(permission);
    }

    public boolean canBeModified() {
        return this.roleType != RoleType.CUSTOM || !this.isActive;
    }

    public void activate() {
        if (roleType == RoleType.SYSTEM) {
            throw new IllegalArgumentException("System roles cannot be activated/deactivated");
        }
        this.isActive = true;
    }

    public void deactivate() {
        if (roleType == RoleType.SYSTEM) {
            throw new IllegalArgumentException("System roles cannot be activated/deactivated");
        }
        this.isActive = false;
    }

    public boolean isActive() {
        return this.isActive;
    }
}
