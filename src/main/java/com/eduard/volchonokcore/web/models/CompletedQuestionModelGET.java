package com.eduard.volchonokcore.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedQuestionModelGET {
    private Integer question_id;
    private Integer test_id;
    private List<SelectedAnswerModel> answers;
}
