package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findAllByLesson(Lesson lesson);
    @Query(value = "select question_id from questions \n" +
            "where test_id = :testId"
            ,nativeQuery = true)
    List<Integer> findAllQuestionsIdsByTestId(@Param("testId") Integer testId);
}
