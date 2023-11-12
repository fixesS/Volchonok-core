package com.eduard.volchonokcore.web.models;

import com.eduard.volchonokcore.database.entities.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionModel {
    private Integer question_id;
    private Integer test_id;
    private String text;
    private String explanation;
    private List<AnswerModel> answers;
}
