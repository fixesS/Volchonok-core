package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Test;
import com.eduard.volchonokcore.database.repositories.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    @Transactional
    public Test findById(Integer id){
        return testRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Test> findAllByLesson(Lesson lesson){
        return testRepository.findAllByLesson(lesson);
    }
    public List<Integer> findAllIdsByLesson(Lesson lesson){
        List<Test> tests = findAllByLesson(lesson);
        List<Integer> testsIds = new ArrayList<>();
        for(Test test: tests){
            testsIds.add(test.getTestId());
        }
        return testsIds;
    }
    @Transactional
    public List<Integer> findAllQuestionIdsByTestId(Integer testId){
        return testRepository.findAllQuestionsIdsByTestId(testId);
    }
    @Transactional
    public List<Test> findAll(){
        return testRepository.findAll();
    }
}
