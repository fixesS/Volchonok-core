package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Course;
import com.eduard.volchonokcore.database.entities.Review;
import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.repositories.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public void create(Review review){
        reviewRepository.save(review);
    }
    @Transactional
    public Review findById(Integer reviewId){
        return reviewRepository.findById(reviewId).orElse(null);
    }
    @Transactional
    public List<Review> findAllByUser(User user){
       return reviewRepository.findAllByUser(user);
    }
    @Transactional
    public List<Review> findAllByCourse(Course course){
        return reviewRepository.findAllByCourse(course);
    }


}
