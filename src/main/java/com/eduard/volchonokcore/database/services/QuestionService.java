package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Question;
import com.eduard.volchonokcore.database.repositories.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Question> findAll(){
        return questionRepository.findAll();
    }
}
