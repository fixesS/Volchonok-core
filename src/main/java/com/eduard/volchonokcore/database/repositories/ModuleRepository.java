package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Course;
import com.eduard.volchonokcore.database.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    List<Module> findAllByCourse(Course course);
    @Query(value = "select question_id  from questions \n" +
            "where test_id in ( \n" +
            "\tselect test_id from tests\n" +
            "\twhere lesson_id in (\n" +
            "\t\tselect lesson_id from lessons\n" +
            "\t\twhere module_id = :moduleId\n" +
            "\t)\n" +
            ")"
            ,nativeQuery = true)
    List<Integer> findAllQuestionIdsByModuleId(@Param("moduleId") Integer moduleId);
    @Query(value = "select test_id from tests\n" +
            "where lesson_id in (\n" +
            "\tselect lesson_id from lessons\n" +
            "\twhere module_id = :moduleId\n" +
            ")"
            ,nativeQuery = true)
    List<Integer> findAllTestsIdsByModuleId(@Param("moduleId") Integer moduleId);
    @Query(value = "select lesson_id from lessons\n" +
            "where module_id = :moduleId"
            ,nativeQuery = true)
    List<Integer> findAllLessonsIdsByModuleId(@Param("moduleId") Integer moduleId);

}
