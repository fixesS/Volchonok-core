package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.services.SessionService;
import com.eduard.volchonokcore.database.services.UserService;
import com.eduard.volchonokcore.security.enums.Role;
import com.eduard.volchonokcore.security.jwt.JwtService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<String> handleGetMe(HttpServletRequest request) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{
                    response = ApiResponse.OK;
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                UserModel userModel = UserModel.builder()
                        .id(user.getUserId())
                        .login(user.getLogin())
                        .avatar(user.getAvatar())
                        .level(user.getLevel())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .address(user.getAddress())
                        .class_grade(user.getClassColumn())
                        .coins(user.getCoins())
                        .build();
                ApiOk<UserModel> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), userModel);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @PostMapping("/me")
    public ResponseEntity<String> handlePostMe(HttpServletRequest request,@Validated @RequestBody UserModel userModel) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;


        UUID sessionUuid =  (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{
                    user.setAvatar(Optional.ofNullable(userModel.getAvatar()).orElse(user.getAvatar()));
                    user.setLevel(Optional.ofNullable(userModel.getLevel()).orElse(user.getLevel()));
                    user.setPhone(Optional.ofNullable(userModel.getPhone()).orElse(user.getPhone()));
                    user.setEmail(Optional.ofNullable(userModel.getEmail()).orElse(user.getEmail()));
                    user.setAddress(Optional.ofNullable(userModel.getAddress()).orElse(user.getAddress()));
                    user.setClassColumn(Optional.ofNullable(userModel.getClass_grade()).orElse(user.getClassColumn()));
                    user.setCoins(Optional.ofNullable(userModel.getCoins()).orElse(user.getCoins()));

                    userService.update(user);

                    response = ApiResponse.OK;
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<String> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), "Ok");
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }

}
