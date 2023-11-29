package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByModule(Module module);
    @Query(value = "select test_id from tests\n" +
            "where lesson_id = :lesson_id"
            ,nativeQuery = true)
    List<Integer> findAllQuestionsIdsByLessonId(@Param("lesson_id") Integer lesson_id);
}
