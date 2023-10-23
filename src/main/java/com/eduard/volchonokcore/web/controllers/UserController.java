package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.web.models.AuthModel;
import com.eduard.volchonokcore.web.models.RegistrationModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @PostMapping
    public ResponseEntity<String> handle(HttpServletRequest request, @RequestBody AuthModel authModel) throws UnknownHostException {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
