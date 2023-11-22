package com.eduard.volchonokcore.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedQuestionModelPOST {
    private Integer question_id;
    private Set<Integer> answers;
}
