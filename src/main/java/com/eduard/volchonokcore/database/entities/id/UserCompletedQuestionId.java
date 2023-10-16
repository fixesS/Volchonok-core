package com.eduard.volchonokcore.database.entities.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
public class UserCompletedQuestionId implements Serializable {

    private Integer userid;

    private Integer questionid;
}
