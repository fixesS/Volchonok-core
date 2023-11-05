package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import com.eduard.volchonokcore.database.entities.UserCourse;
import com.eduard.volchonokcore.database.entities.id.UserCompletedQuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCompletedQuestionRepository extends JpaRepository<UserCompletedQuestion, Integer> {
    UserCompletedQuestion findByUserid(User user);
}
