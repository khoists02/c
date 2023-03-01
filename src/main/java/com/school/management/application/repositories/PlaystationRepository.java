package com.school.management.application.repositories;

import com.school.management.application.model.Playstation;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaystationRepository extends BaseRepository<Playstation, UUID> {
}
