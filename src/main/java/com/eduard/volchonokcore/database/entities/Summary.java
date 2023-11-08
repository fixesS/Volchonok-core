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
@Table(name = "summary", schema = "public")
public class Summary {
    @Id
    @Column(name = "summaryid")
    private Integer summaryId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lessonid")
    private Lesson lesson;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "chat_text")
    private String chatText;
    @Column
    private String video;
    @Column
    private Integer duration;

}
