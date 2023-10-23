package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void create(User user){
        userRepository.save(user);
    }
    @Transactional
    public void update(User user){
        userRepository.save(user);
    }
    @Transactional
    public User findById(Integer id){
       return userRepository.findById(id).orElse(null);
    }
    @Transactional
    public User findByLogin(String login){
        return userRepository.findByLogin(login).orElse(null);
    }
    @Transactional
    public List<User> findAll(){
        return userRepository.findAll();
    }
}
