package com.eduard.volchonokcore.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionModel {
    private Integer question_id;
    private Integer test_id;
    private String text;
    private String explanation;
    private String answers;
}
