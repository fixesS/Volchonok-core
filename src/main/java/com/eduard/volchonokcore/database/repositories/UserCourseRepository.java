package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.UserCourse;
import com.eduard.volchonokcore.database.entities.id.UserCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {
}
