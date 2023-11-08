package com.eduard.volchonokcore.web.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private Integer id;
    private String login;
    private String firstname;
    private String surname;
    private String midllename;

    private String avatar;
    private Integer level;
    private String phone;
    private String email;
    private String address;
    private Integer class_grade;
    private Integer coins;

}
