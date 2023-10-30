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
@Table(name = "test", schema = "public")
public class Test {
    @Id
    @Column(name = "testid")
    private Integer testId;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "lessonid")
    private Lesson lesson;

}
