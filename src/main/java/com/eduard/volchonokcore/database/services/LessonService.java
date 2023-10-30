package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Module;
import com.eduard.volchonokcore.database.repositories.LessonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    public Lesson finById(Integer id){
        return lessonRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Lesson> findAllByModule(Module module){
        return lessonRepository.findAllByModule(module);
    }
    public List<Integer> findAllIdsByModule(Module module){
        List<Lesson> lessons = findAllByModule(module);
        List<Integer> lessonsIds = new ArrayList<>();
        for(Lesson lesson: lessons){
            lessonsIds.add(lesson.getLessonId());
        }
        return lessonsIds;
    }
    @Transactional
    public List<Lesson> findAll(Integer id){
        return lessonRepository.findAll();
    }
}
