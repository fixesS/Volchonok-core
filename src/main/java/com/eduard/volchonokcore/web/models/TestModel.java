package com.eduard.volchonokcore.web.models;

import com.eduard.volchonokcore.database.entities.Lesson;
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
public class TestModel {
    private Integer test_id;

    private String text;

    private Integer lesson_id;
}
