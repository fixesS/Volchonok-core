package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCourse;
import com.eduard.volchonokcore.database.repositories.UserCourseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCourseService {
    @Autowired
    private UserCourseRepository userCourseRepository;

    @Transactional
    public void createUserCourse(UserCourse userCourse){
        userCourseRepository.save(userCourse);
    }
    public void updateUserCourse(UserCourse userCourse){
        userCourseRepository.save(userCourse);
    }
    @Transactional
    public UserCourse findById(Integer id){
        return userCourseRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<UserCourse> findAll(){
        return userCourseRepository.findAll();
    }
    @Transactional
    public UserCourse findByUser(User user){
        return userCourseRepository.findByUser(user);
    }
}
