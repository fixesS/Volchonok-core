package com.eduard.volchonokcore.web.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthModel {
    private String login;
    private String password;
}
