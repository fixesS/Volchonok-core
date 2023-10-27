package com.eduard.volchonokcore.database.entities;

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
@Table(name = "lessons", schema = "public")
public class Lesson {
    @Id
    private Integer lessonid;

    @Column(name = "chat_text")
    private String chatText;
    @Column(name = "abstract_text")
    private String abstractText;
    @ManyToOne
    @JoinColumn(name = "moduleid")
    private Module module;
    @Column
    private Integer number;
}
