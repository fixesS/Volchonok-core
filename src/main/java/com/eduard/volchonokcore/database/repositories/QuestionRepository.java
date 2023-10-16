package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
