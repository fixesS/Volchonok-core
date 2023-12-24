package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.SelectedAnswers;
import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectedAnswerRepository extends JpaRepository<SelectedAnswers, Integer> {
    @Modifying
    @Query(value = "delete from selected_answers \n" +
            "where completed_questions_id in ( select cq.id from users_completed_questions as cq\n" +
            "\twhere cq.completed_test_id in (select ct.id from users_completed_tests as ct\n" +
            "\twhere ct.test_id = :testId\n" +
            "\t\t)\n" +
            "\t)"
            ,nativeQuery = true)
    void deleteAllByTestId(@Param("testId") Integer testId);
    List<SelectedAnswers> findAllByUserCompletedQuestion(UserCompletedQuestion userCompletedQuestion);
}
