package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.UserCourse;
import com.eduard.volchonokcore.database.entities.id.UserCourseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseRepository extends JpaRepository<UserCourse, UserCourseId> {
}
