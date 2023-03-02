package com.school.management.application.model;

import com.school.management.application.support.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "role_permissions")
public class RolePermission extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public RolePermission() {}

    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, permission);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == null) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RolePermission that = (RolePermission) obj;
        return Objects.equals(role.getId(), that.role.getId()) && Objects.equals(permission.getId(), that.permission.getId());
    }
}
