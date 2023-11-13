package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.Course;
import com.eduard.volchonokcore.database.entities.Review;

import com.eduard.volchonokcore.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByUser(User user);
    List<Review> findAllByCourse(Course course);
}
