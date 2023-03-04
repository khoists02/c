package com.school.management.application.repositories;

import com.school.management.application.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends BaseRepository<User, UUID>{
    public User findByUsername(String username);

    Optional<User> findOneByUsername(String username);

    @Query("""
            select case when (count(ur.id) > 0) then true else false end 
            from UserRole ur 
            join ur.role role
            join role.rolePermissions rolePermission 
            where ur.user = :user and rolePermission.permission.code = :permission
            """)
    boolean hasPermission(@Param("user") User user, @Param("permission") String permission);
}
