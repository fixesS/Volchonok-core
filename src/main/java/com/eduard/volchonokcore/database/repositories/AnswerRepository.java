package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.*;
import com.eduard.volchonokcore.database.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAllByQuestion(Question question);

}
