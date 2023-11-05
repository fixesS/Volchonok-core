package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import com.eduard.volchonokcore.database.entities.UserCourse;
import com.eduard.volchonokcore.database.repositories.UserCompletedQuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCompletedQuestionService {

    @Autowired
    private UserCompletedQuestionRepository userCompletedQuestionRepository;

    @Transactional
    private UserCompletedQuestion findById(Integer id){
        return userCompletedQuestionRepository.findById(id).orElse(null);
    }
    @Transactional
    public void createUserCourse(UserCompletedQuestion userCompletedQuestion){
        userCompletedQuestionRepository.save(userCompletedQuestion);
    }
    @Transactional
    public void updateUserCourse(UserCompletedQuestion userCompletedQuestion){
        userCompletedQuestionRepository.save(userCompletedQuestion);
    }
    @Transactional
    public List<UserCompletedQuestion> findAll(){
        return userCompletedQuestionRepository.findAll();
    }
    @Transactional
    public UserCompletedQuestion findByUser(User user){
        return userCompletedQuestionRepository.findByUserid(user);
    }
}
