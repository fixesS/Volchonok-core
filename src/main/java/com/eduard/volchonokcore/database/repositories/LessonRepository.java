package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
}
