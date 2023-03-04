package com.school.management.application.repositories;

import com.school.management.application.model.User;
import com.school.management.application.model.UserSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserSessionRepository extends BaseRepository<UserSession, UUID> {
    Optional<UserSession> findOneByUserAndId(User user, UUID sessionId);
    Page<UserSession> findAllByUserAndExpiresAtIsAfter(User user, ZonedDateTime now, Pageable pageable);
    void deleteAllByUserAndIdNot(User user, UUID sessionId);
}
