package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import com.eduard.volchonokcore.database.entities.UserCompletedTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCompletedTestRepository extends JpaRepository<UserCompletedTest, Integer>{
    List<UserCompletedTest> findAllByUserid(Integer userid);
    Optional<UserCompletedTest> findFirstByUseridAndTestid(Integer userid, Integer testid);

}
