package com.school.management.application.model;

import com.school.management.application.support.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

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

}
