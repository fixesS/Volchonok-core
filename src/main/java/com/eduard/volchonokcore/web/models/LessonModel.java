package com.eduard.volchonokcore.web.models;

import com.eduard.volchonokcore.database.entities.Module;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonModel {
    private Integer lesson_id;
    private String name;
    private String description;
    private Integer module_id;
    private Integer number;
    private String chat_text;
    private Integer duration;
    private String video;
}
