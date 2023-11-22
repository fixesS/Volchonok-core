package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.SelectedAnswers;
import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectedAnswerRepository extends JpaRepository<SelectedAnswers, Integer> {
    List<SelectedAnswers> findAllByUserCompletedQuestion(UserCompletedQuestion userCompletedQuestion);
}
