package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.entities.Summary;
import com.eduard.volchonokcore.database.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummaryRepository extends JpaRepository<Summary, Integer> {
    List<Summary> findAllByLesson(Lesson lesson);
}
