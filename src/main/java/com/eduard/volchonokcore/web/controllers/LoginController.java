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
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    public ResponseEntity<String> handle(HttpServletRequest request, @RequestBody AuthModel authModel) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");
        String ip_address = request.getRemoteAddr();
        GsonParser gsonParser = new GsonParser();
        String body = "";
        ApiResponse response;

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authModel.getLogin(), authModel.getPassword());
        Authentication auth = authenticationManager.authenticate(authenticationToken);

        User user1 = (User)auth.getPrincipal();
        User user = userService.findByLogin(user1.getLogin());
        response = ApiResponse.OK;
        if(user==null){
            response = ApiResponse.USER_DOES_NOT_EXIST;
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
