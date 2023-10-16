package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
