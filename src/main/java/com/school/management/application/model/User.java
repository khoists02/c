package com.school.management.application.model;

import com.school.management.application.support.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "compare_password")
    private String comparePassword;

    @NotNull
    @Email(message = "Invalid Email Address.")
    @Column(name = "email")
    private String email;

    @Nullable
    @Column(name = "owner")
    private Boolean owner;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy ="user",
            cascade = {CascadeType.ALL}
    )
    private Set<UserRole> userRoles = new HashSet<>();

    public void addRole(Role role) { this.userRoles.add(new UserRole(this, role)); };

    public void removeRole(Role role) {  this.userRoles.remove(new UserRole(this, role)); }

    public void addRoles(Set<Role> roles) {
        this.userRoles.addAll(
                roles.stream().map(r -> new UserRole(this, r)).collect(Collectors.toSet())
        );
    }

    public void setRoles(Set<Role> roles) {
        this.userRoles.retainAll(roles.stream().map(r -> new UserRole(this, r)).collect(Collectors.toSet()));
    }

    public Set<Role> getRoles() {
        return this.userRoles.stream().map(UserRole::getRole).collect(Collectors.toSet());
    }

}
