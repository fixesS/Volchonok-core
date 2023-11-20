package com.eduard.volchonokcore.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedQuestionModelGET {
    private Integer question_id;
    private Integer answer_id;
    private Integer test_id;
    private Boolean is_right;
    private String explanation;
}
