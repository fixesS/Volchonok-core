package com.eduard.volchonokcore.config;

import com.eduard.volchonokcore.database.services.SessionService;
import com.eduard.volchonokcore.security.AuthService;
import com.eduard.volchonokcore.security.encryption.UserDataEncryptor;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VolchonokConfig {
    @Autowired
    public JwtConfig jwtConfig;

    @Bean
    public UserDataEncryptor userDataEncryptor() {
        return new UserDataEncryptor();
    }

    @Bean
    public AuthService authService(){
        return new AuthService(jwtConfig.jwtService());
    }
}
