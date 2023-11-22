package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import com.eduard.volchonokcore.database.entities.UserCompletedTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCompletedQuestionRepository extends JpaRepository<UserCompletedQuestion, Integer> {
    List<UserCompletedQuestion> findAllByUserCompletedTest(UserCompletedTest userCompletedTest);
}
