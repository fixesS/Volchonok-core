package com.eduard.volchonokcore.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions", schema = "public")
public class Question {
    @Id
    private Integer questionId;
    @Column
    @ManyToOne
    @JoinColumn(name = "lessonid")
    private Lesson lesson;
    @Column
    private String text;
    @Column
    private String trueAnswer;
    @Column
    private String falseAnswer1;
    @Column
    private String falseAnswer2;
    @Column
    private String falseAnswer3;

}
