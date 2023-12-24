package com.eduard.volchonokcore.database.repositories;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.entities.UserCompletedQuestion;
import com.eduard.volchonokcore.database.entities.UserCompletedTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCompletedTestRepository extends JpaRepository<UserCompletedTest, Integer>{
    @Modifying
    @Query(value = "delete from users_completed_tests \n" +
            "\twhere test_id = :testId"
            ,nativeQuery = true)
    void deleteAllByTestId(@Param("testId") Integer testId);
    List<UserCompletedTest> findAllByUserid(Integer userid);
    Optional<UserCompletedTest> findFirstByUseridAndTestid(Integer userid, Integer testid);

}
