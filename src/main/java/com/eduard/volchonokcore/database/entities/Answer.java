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
@Table(name = "answers", schema = "public")
public class Answer {
    @Id
    @Column(name = "answerid")
    private Integer answerId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "questionid")
    private Question question;
    @Column
    private String text;
    @Column(name = "is_right")
    private Boolean isRight;
}
