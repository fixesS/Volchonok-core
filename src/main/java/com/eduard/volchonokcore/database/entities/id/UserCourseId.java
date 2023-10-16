package com.eduard.volchonokcore.database.entities.id;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class UserCourseId implements Serializable {
    private Integer userid;
    private Integer courseid;
}
