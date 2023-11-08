package com.eduard.volchonokcore.web.models;

import com.eduard.volchonokcore.database.entities.Lesson;
import jakarta.persistence.CascadeType;
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
public class SummaryModel {
    private Integer summary_id;
    private Integer lesson_id;
    private String name;
    private String description;
    private String chat_text;
    private String video;
    private Integer duration;
}
