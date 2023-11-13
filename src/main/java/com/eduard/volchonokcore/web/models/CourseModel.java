package com.eduard.volchonokcore.web.models;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseModel {
    private Integer course_id;
    private String name;
    private String description;
    private List<ReviewModel> reviews;
}
