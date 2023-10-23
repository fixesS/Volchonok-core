package com.eduard.volchonokcore.web.models;

import lombok.Data;

@Data
public class TokenData {
    private String access_token;
    private String refresh_token;
}
