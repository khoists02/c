package com.school.management.application.model;

import com.school.management.application.support.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission extends BaseEntity {

    @NotNull
    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "groupKey")
    private String groupKey;

    @OneToMany(
            mappedBy = "permission",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<RolePermission> rolePermissions = new HashSet<>();
}
