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
@Table(name = "lessons", schema = "public")
public class Lesson {
    @Id
    @Column(name = "lesson_id")
    private Integer lessonId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private Module module;
    @Column
    private Integer number;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "chat_text")
    private String chatText;
    @Column
    private Integer duration;
}
