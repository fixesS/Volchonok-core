package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query(value = "select question_id  from questions \n" +
            "where test_id in ( \n" +
            "\tselect test_id from tests\n" +
            "\twhere lesson_id in (\n" +
            "\t\tselect lesson_id from lessons\n" +
            "\t\twhere module_id in (\n" +
            "\t\t\tselect module_id from modules \n" +
            "\t\t\twhere course_id = :courseId\n" +
            "\t\t)\n" +
            "\t)\n" +
            ")"
            ,nativeQuery = true)
    List<Integer> findAllQuestionsIdsByCourseId(@Param("courseId") Integer courseId);
    @Query(value = "select test_id from tests\n" +
            "where lesson_id in (\n" +
            "\tselect lesson_id from lessons\n" +
            "\twhere module_id in (\n" +
            "\t\tselect module_id from modules \n" +
            "\t\twhere course_id = :courseId\n" +
            "\t)\n" +
            ")\n", nativeQuery = true)
    List<Integer> findAllTestsIdsByCourseId(@Param("courseId") Integer courseId);
    @Query(value = "select lesson_id from lessons\n" +
            "where module_id in (\n" +
            "\tselect module_id from modules \n" +
            "\twhere course_id = :courseId\n" +
            ")\n", nativeQuery = true)
    List<Integer> findAllLessonsIdsByCourseId(@Param("courseId") Integer courseId);
    @Query(value = "select module_id from modules \n" +
            "where course_id = :courseId"
            ,nativeQuery = true)
    List<Integer> findAllModulesIdsByCourseId(@Param("courseId") Integer courseId);
}
