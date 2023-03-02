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
@Table(name = "user_roles")
public class UserRole extends BaseEntity implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public UserRole() {}

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == null) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserRole that = (UserRole) obj;
        return Objects.equals(role.getId(), that.role.getId()) && Objects.equals(user.getId(), that.user.getId());
    }
}
