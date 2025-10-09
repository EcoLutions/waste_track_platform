package com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.Roles;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private Roles name;

    public Role(Roles name) {
        super();
        this.name = name;
    }

    public static Role getDefaultRole() { return new Role(Roles.ROLE_CITIZEN); }

    public static Role toRoleFromName(String name) {
        return new Role(Roles.valueOf(name));
    }

    public static List<Role> validateRoleSet(List<Role> roles) {
        return roles == null || roles.isEmpty() ? List.of(getDefaultRole()) : roles;
    }

    public String getStringName() {
        return name.name();
    }
}
