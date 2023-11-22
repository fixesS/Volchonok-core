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
@Table(name = "tests", schema = "public")
public class Test {
    @Id
    @Column(name = "test_id")
    private Integer testId;
    @Column
    private String name;
    @Column
    private String description;

    @Column
    private String text;
    @Column
    private Integer duration;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

}
