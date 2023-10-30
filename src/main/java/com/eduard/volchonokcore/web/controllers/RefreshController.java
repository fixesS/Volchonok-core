package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.services.SessionService;
import com.eduard.volchonokcore.database.services.UserService;
import com.eduard.volchonokcore.security.AuthService;
import com.eduard.volchonokcore.security.jwt.JwtService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.eduard.volchonokcore.web.models.RegistrationModel;
import com.eduard.volchonokcore.web.models.TokenData;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth/refresh")

public class RefreshController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthService authService;
    @Autowired
    private SessionService sessionService;

    @PostMapping
    public ResponseEntity<String> handle(@RequestBody TokenData requestTokenData) throws UnknownHostException {
        String refreshToken = requestTokenData.getRefresh_token();
        ApiResponse response;
        TokenData tokenData = null;
        String body = "";
        GsonParser gsonParser = new GsonParser();
        Claims claims = jwtService.getRefreshClaims(refreshToken);
        try {
            if (!jwtService.validateRefreshToken(refreshToken) || !claims.getSubject().equals("refresh")) {
                response = ApiResponse.INVALID_REFRESH_TOKEN;
            } else {
                Session session = sessionService.findByUuid(jwtService.getSessionIdRefresh(refreshToken));
                if (session == null) {
                    response = ApiResponse.SESSION_DOES_NOT_EXIST;
                } else {
                    Date iat = claims.getIssuedAt();
                    if (!iat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
                        tokenData = authService.refreshSession(session.getSessionUuid());
                        response = ApiResponse.OK;
                    } else {
                        response = ApiResponse.SESSION_EXPIRED;
                    }
                }
            }
        }catch (Exception e){
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<TokenData> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), tokenData);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body, response.getStatus());
    }

}
