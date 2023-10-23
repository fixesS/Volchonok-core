package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.repositories.LessonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    private Lesson finById(Integer id){
        return lessonRepository.findById(id).orElse(null);
    }
    @Transactional
    private List<Lesson> findAll(Integer id){
        return lessonRepository.findAll();
    }
}
