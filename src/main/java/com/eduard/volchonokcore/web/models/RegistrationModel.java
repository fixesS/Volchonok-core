package com.eduard.volchonokcore.web.models;

import lombok.Data;

@Data
public class RegistrationModel {
    private String login;
    private String password;
    private String phone;
    private String email;
    private String address;
    private Integer class_grade;
    private String coins;

}

