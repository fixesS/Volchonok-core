package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.services.UserService;
import com.eduard.volchonokcore.security.AuthService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.eduard.volchonokcore.web.models.AuthModel;
import com.eduard.volchonokcore.web.models.TokenData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/api/v1/auth/login")
@Tag(name="Login controller", description="Handles login requests")
public class LoginController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    @Operation(
            summary = "Login",
            description = "Auth by login and password"
    )
    public ResponseEntity<String> handle(HttpServletRequest request, @RequestBody AuthModel authModel) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");
        String ip_address = request.getRemoteAddr();
        GsonParser gsonParser = new GsonParser();
        String body = "";
        ApiResponse response;

        User user = userService.findByLogin(authModel.getLogin());

        try {
            if (user == null) {
                response = ApiResponse.USER_DOES_NOT_EXIST;
            } else {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(authModel.getLogin(), authModel.getPassword());
                Authentication auth = authenticationManager.authenticate(authenticationToken);


                user = (User) auth.getPrincipal();
                response = ApiResponse.OK;
                if (user == null) {
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
            }
        }catch (Exception e){
            response = ApiResponse.UNKNOWN_ERROR;
            if(e instanceof BadCredentialsException){
                response = ApiResponse.USER_DOES_NOT_EXIST;
            }
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
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
        return new ResponseEntity<>(body,response.getStatus());
    }
}
