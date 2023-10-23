package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {
    UserCourse findByUser(User user);
}
