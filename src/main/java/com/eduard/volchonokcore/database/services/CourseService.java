package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Course;
import com.eduard.volchonokcore.database.repositories.CourseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public Course findById(Integer id){
        return courseRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Course> findAll(){
        return courseRepository.findAll();
    }
    @Transactional
    public Course update(Course course){
        return courseRepository.save(course);
    }
    @Transactional
    public List<Integer> findAllQuestionIdsByCourseId(Integer courseId){
        return courseRepository.findAllQuestionsIdsByCourseId(courseId);
    }
    @Transactional
    public List<Integer> findAllTestsIdsByCourseId(Integer courseId){
        return courseRepository.findAllTestsIdsByCourseId(courseId);
    }
    @Transactional
    public List<Integer> findAllLessonsIdsByCourseId(Integer courseId){
        return courseRepository.findAllLessonsIdsByCourseId(courseId);
    }
    @Transactional
    public List<Integer> findAllModulesIdsByCourseId(Integer courseId){
        return courseRepository.findAllModulesIdsByCourseId(courseId);
    }
}
