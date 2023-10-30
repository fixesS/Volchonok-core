package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Question;
import com.eduard.volchonokcore.database.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findAllByTest(Test test);
}
