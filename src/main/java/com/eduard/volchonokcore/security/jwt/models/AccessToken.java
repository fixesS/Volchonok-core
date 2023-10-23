package com.eduard.volchonokcore.security.jwt.models;

import lombok.Data;

import java.util.Date;

@Data
public class AccessToken {
    private String accessToken;
    private Date expiresIn;
    private Date created;

}
