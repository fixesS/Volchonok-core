package com.eduard.volchonokcore.database.entities;

import com.eduard.volchonokcore.database.entities.id.UserCompletedQuestionId;
import com.eduard.volchonokcore.database.entities.id.UserCourseId;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
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
@Table(name = "users_completed_questions", schema = "public")
public class UserCompletedQuestion {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_test_id")
    private UserCompletedTest userCompletedTest;
    @Column(name = "is_right")
    private Boolean isRight;

}
