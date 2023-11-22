package com.eduard.volchonokcore.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleModel {
    private Integer module_id;
    private Integer course_id;
    private Integer number;
    private String name;
    private String description;
}
