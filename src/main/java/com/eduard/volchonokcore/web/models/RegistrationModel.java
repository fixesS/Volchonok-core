package com.eduard.volchonokcore.web.models;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class RegistrationModel {
    @NotNull
    private String login;
    @NotNull
    private String password;
    private String phone;
    private String email;
    private String address;
    private Integer class_grade;
    private String coins;

}

