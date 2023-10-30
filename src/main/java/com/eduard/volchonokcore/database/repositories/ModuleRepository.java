package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Course;
import com.eduard.volchonokcore.database.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    List<Module> findAllByCourse(Course course);
}
