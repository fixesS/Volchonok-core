package com.eduard.volchonokcore.config;

import com.eduard.volchonokcore.security.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JwtConfig jwtConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/auth/*").permitAll()
                        .requestMatchers("/api/v1/user/**").hasAuthority(Role.USER.name())
                        .requestMatchers("/api/v1/admin/**").hasAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated()
                        )
                .sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(jwtConfig.authenticationManager())
                .addFilterAfter(jwtConfig.jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
        ;
        return http.build();
    }
}