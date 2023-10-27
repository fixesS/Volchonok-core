package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.services.UserService;
import com.eduard.volchonokcore.security.AuthService;
import com.eduard.volchonokcore.security.enums.Role;
import com.eduard.volchonokcore.security.jwt.JwtService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.eduard.volchonokcore.web.models.RegistrationModel;
import com.eduard.volchonokcore.web.models.TokenData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth/registration")
public class RegistrationController {//todo оставить login password - остальное null
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<String> handle(HttpServletRequest request, @Validated @RequestBody RegistrationModel registrationModel) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");
        String ip_address = request.getRemoteAddr();
        GsonParser gsonParser = new GsonParser();
        User user = null;
        String body = "";
        ApiResponse response;
        log.info(registrationModel.toString());
        try {
            int coins = 0;
            if(registrationModel.getCoins()!=null){
                coins = Integer.valueOf(registrationModel.getCoins());
            }
            user = User.builder()
                    .login(registrationModel.getLogin())
                    .password(passwordEncoder.encode(registrationModel.getPassword()))
                    .address(registrationModel.getAddress())
                    .role(Role.USER)
                    .phone(registrationModel.getPhone())
                    .email(registrationModel.getEmail())
                    .classColumn(registrationModel.getClass_grade())
                    .coins(coins)
                    .build();
            userService.create(user);
            response = ApiResponse.OK;
        } catch (Exception e) {
            log.error(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                TokenData tokenData = authService.createSession(user,userAgent,InetAddress.getByName(ip_address));
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
