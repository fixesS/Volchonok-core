package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Question;
import com.eduard.volchonokcore.database.entities.Test;
import com.eduard.volchonokcore.database.repositories.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Transactional
    public Question findById(Integer id){
        return questionRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Question> findAllByTest(Test test){
        return questionRepository.findAllByTest(test);
    }
    public List<Integer> findAllIdsByTest(Test test){
        List<Question> questions = findAllByTest(test);
        List<Integer> questionsIds = new ArrayList<>();
        for(Question question: questions){
            questionsIds.add(question.getQuestionId());
        }
        return questionsIds;
    }
    @Transactional
    public List<Question> findAll(){
        return questionRepository.findAll();
    }
}
