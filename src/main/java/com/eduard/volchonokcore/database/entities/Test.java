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
    private String name;
    @Column
    private String description;

    @Column
    private String text;
    @Column
    private Integer duration;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lessonid")
    private Lesson lesson;

}
