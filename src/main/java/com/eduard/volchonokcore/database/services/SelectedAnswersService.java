package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.SelectedAnswers;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import com.eduard.volchonokcore.database.entities.UserCompletedTest;
import com.eduard.volchonokcore.database.repositories.SelectedAnswerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectedAnswersService {
    @Autowired
    private SelectedAnswerRepository selectedAnswerRepository;

    @Transactional
    private SelectedAnswers findById(Integer id){
        return selectedAnswerRepository.findById(id).orElse(null);
    }
    @Transactional
    public void createUserCourse(SelectedAnswers userCompletedQuestion){
        selectedAnswerRepository.save(userCompletedQuestion);
    }
    @Transactional
    public void createAll(List<SelectedAnswers> completedQuestions){
        selectedAnswerRepository.saveAll(completedQuestions);
    }
    @Transactional
    public void updateUserCourse(SelectedAnswers userCompletedQuestion){
        selectedAnswerRepository.save(userCompletedQuestion);
    }
    @Transactional
    public List<SelectedAnswers> findAll(){
        return selectedAnswerRepository.findAll();
    }
    @Transactional
    public List<SelectedAnswers> findAllByUserCompletedQuestion(UserCompletedQuestion userCompletedQuestion){
        return selectedAnswerRepository.findAllByUserCompletedQuestion(userCompletedQuestion);
    }
}
