package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
