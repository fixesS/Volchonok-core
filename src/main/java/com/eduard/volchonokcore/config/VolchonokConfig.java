package com.eduard.volchonokcore.config;

import com.eduard.volchonokcore.database.services.SessionService;
import com.eduard.volchonokcore.security.AuthService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VolchonokConfig {
    @Autowired
    public JwtConfig jwtConfig;

    @Bean
    public AuthService authService(){
        return new AuthService(jwtConfig.jwtService());
    }
}
