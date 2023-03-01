package com.school.management.application.repositories;

import com.school.management.application.model.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends BaseRepository<User, UUID>{
}
