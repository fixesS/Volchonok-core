package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.*;
import com.eduard.volchonokcore.database.repositories.UserCompletedTestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCompletedTestService {
    @Autowired
    private UserCompletedTestRepository userCompletedTestRepository;

    @Transactional
    private UserCompletedTest findById(Integer id){
        return userCompletedTestRepository.findById(id).orElse(null);
    }
    @Transactional
    public void createUserCompletedTest(UserCompletedTest userCompletedTest){
        userCompletedTestRepository.save(userCompletedTest);
    }
    @Transactional
    public void createAll(List<UserCompletedTest> completedTests){
        userCompletedTestRepository.saveAll(completedTests);
    }
    @Transactional
    public void updateUserCompletedTest(UserCompletedTest userCompletedTest){
        userCompletedTestRepository.save(userCompletedTest);
    }
    @Transactional
    public List<UserCompletedTest> findAll(){
        return userCompletedTestRepository.findAll();
    }
    @Transactional
    public List<UserCompletedTest> findAllByUser(User user){
        return userCompletedTestRepository.findAllByUserid(user.getUserId());
    }
    @Transactional
    public UserCompletedTest findAllByUserAndTest(User user, Test test){
        return userCompletedTestRepository.findFirstByUseridAndTestid(user.getUserId(), test.getTestId()).orElse(null);
    }
}
