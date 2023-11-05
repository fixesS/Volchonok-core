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
    @Column(name = "questionid")
    private Integer questionId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "testid")
    private Test test;
    @Column
    private String text;

    @Column
    private String explanation;

    @Column
    private String answers;

}
