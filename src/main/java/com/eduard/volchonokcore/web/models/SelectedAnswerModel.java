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
public class SelectedAnswerModel {

    private Integer answer_id;
    private String text;
    private Boolean is_right;
    private String explanation;
}
