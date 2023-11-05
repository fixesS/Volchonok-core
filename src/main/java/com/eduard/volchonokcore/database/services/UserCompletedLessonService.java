package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCompletedLesson;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import com.eduard.volchonokcore.database.repositories.UserCompletedLessonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCompletedLessonService  {
    @Autowired
    private UserCompletedLessonRepository userCompletedLessonRepository;
    @Transactional
    private UserCompletedLesson findById(Integer id){
        return userCompletedLessonRepository.findById(id).orElse(null);
    }
    @Transactional
    public void createUserCourse(UserCompletedLesson userCompletedLesson){
        userCompletedLessonRepository.save(userCompletedLesson);
    }
    @Transactional
    public void updateUserCourse(UserCompletedLesson userCompletedLesson){
        userCompletedLessonRepository.save(userCompletedLesson);
    }
    @Transactional
    public List<UserCompletedLesson> findAll(){
        return userCompletedLessonRepository.findAll();
    }
    @Transactional
    public UserCompletedLesson findByUser(User user){
        return userCompletedLessonRepository.findByUserid(user);
    }
}
