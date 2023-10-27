package com.eduard.volchonokcore.database.entities;

import jakarta.persistence.*;

@Entity
public class Test {
    @Id
    private Integer testid;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "lessonid")
    private Lesson lesson;

}
