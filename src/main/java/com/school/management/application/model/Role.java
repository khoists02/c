package com.school.management.application.model;

import com.school.management.application.support.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private String name;

    @OneToMany(
            mappedBy = "role",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<RolePermission> rolePermissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users = new HashSet<>();

    public void addRolePermission(RolePermission rolePermission) {
        rolePermission.setRole(this);
        this.rolePermissions.add(rolePermission);
    }
    public void setPermissions(Set<Permission> permissions) {
        this.rolePermissions.retainAll(permissions.stream().map(p -> new RolePermission(this, p)).collect(Collectors.toSet()));
        this.addPermissions(permissions);
    }
    public void addPermissions(Set<Permission> permissions) {
        this.rolePermissions.addAll(permissions.stream().map(permission ->new RolePermission(this, permission)).collect(Collectors.toSet()));
    }
    public Set<Permission> getPermissions() {
        return this.rolePermissions.stream().map(x -> x.getPermission()).collect(Collectors.toSet());
    }


}
