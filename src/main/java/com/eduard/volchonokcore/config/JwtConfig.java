package com.eduard.volchonokcore.config;

import com.eduard.volchonokcore.database.repositories.UserRepository;
import com.eduard.volchonokcore.database.services.SessionService;
import com.eduard.volchonokcore.database.services.UserService;
import com.eduard.volchonokcore.security.UserDetailsServiceImpl;
import com.eduard.volchonokcore.security.jwt.JwtFilter;
import com.eduard.volchonokcore.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JwtConfig {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionService sessionService;

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl(userRepository);
    }
    @Bean
    public JwtService jwtService(){
        return new JwtService();
    }
    @Bean
    public JwtFilter jwtAuthenticationFilter(){
        return new JwtFilter(jwtService(),sessionService);
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }
}
