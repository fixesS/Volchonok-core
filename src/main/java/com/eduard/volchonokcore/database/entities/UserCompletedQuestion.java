package com.eduard.volchonokcore.database.entities;

import com.eduard.volchonokcore.database.entities.id.UserCompletedQuestionId;
import com.eduard.volchonokcore.database.entities.id.UserCourseId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usercourses", schema = "public")
@IdClass(UserCompletedQuestionId.class)
public class UserCompletedQuestion {
    @Id
    @OneToOne
    @JoinColumn(name = "userid")
    private User user;
    @Id
    private Integer questionid;
    @OneToMany
    @JoinColumn(name = "questionid")
    private List<Question> questions;
}
