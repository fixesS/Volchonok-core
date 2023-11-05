package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCompletedLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCompletedLessonRepository extends JpaRepository<UserCompletedLesson, Integer> {
    UserCompletedLesson findByUserid(User user);
}
