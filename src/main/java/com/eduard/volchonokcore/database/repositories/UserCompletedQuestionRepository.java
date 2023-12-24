package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import com.eduard.volchonokcore.database.entities.UserCompletedTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCompletedQuestionRepository extends JpaRepository<UserCompletedQuestion, Integer> {
    @Modifying
    @Query(value = "delete from users_completed_questions \n" +
            "\twhere completed_test_id in (select ct.id from users_completed_tests as ct\n" +
            "\twhere ct.test_id = :testId\n" +
            "\t\t)"
            ,nativeQuery = true)
    void deleteAllByTestId(@Param("testId") Integer testId);
    List<UserCompletedQuestion> findAllByUserCompletedTest(UserCompletedTest userCompletedTest);
}
