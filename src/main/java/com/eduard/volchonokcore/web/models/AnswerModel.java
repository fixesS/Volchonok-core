package com.eduard.volchonokcore.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerModel {
    private Integer id;
    private String text;
    private boolean is_right;
}
