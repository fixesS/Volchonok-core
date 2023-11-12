package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Answer;
import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Question;
import com.eduard.volchonokcore.database.entities.Test;
import com.eduard.volchonokcore.database.repositories.AnswerRepository;
import com.eduard.volchonokcore.database.repositories.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    public Answer findById(Integer id){
        return answerRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Answer> findAllByQuestion(Question question){
        return answerRepository.findAllByQuestion(question);
    }
    public List<Integer> findAllIdsByQuestion(Question question){
        List<Answer> answers = findAllByQuestion(question);
        List<Integer> answersIds = new ArrayList<>();
        for(Answer answer: answers){
            answersIds.add(answer.getAnswerId());
        }
        return answersIds;
    }
    @Transactional
    public List<Answer> findAll(){
        return answerRepository.findAll();
    }
}
