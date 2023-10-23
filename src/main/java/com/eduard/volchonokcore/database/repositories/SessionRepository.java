package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findBySessionUuid(UUID uuid);
}
