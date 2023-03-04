package com.school.management.application.model;

import com.school.management.application.support.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_sessions")
public class UserSession extends BaseEntity {
    @ManyToOne
    private User user;
    private String userAgent;
    private String ip;
    private ZonedDateTime expiresAt;
    @CreationTimestamp
    private ZonedDateTime createdAt;
    private Boolean permitExtension = true;
}
