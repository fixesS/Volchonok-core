package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import com.eduard.volchonokcore.database.entities.id.UserCompletedQuestionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCompletedQuestionRepository extends JpaRepository<UserCompletedQuestion, UserCompletedQuestionId> {
}
