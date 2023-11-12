package com.eduard.volchonokcore.config;

import com.eduard.volchonokcore.security.CustomAuthentiactionFailureHandler;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JwtConfig jwtConfig;
    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/api/v1/auth/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers(AUTH_WHITE_LIST).permitAll()
                                .requestMatchers("/api/v1/user/**").hasAuthority(Role.USER.name())
                                .requestMatchers("/api/v1/question/**").hasAuthority(Role.USER.name())
                                .requestMatchers("/api/v1/test/**").hasAuthority(Role.USER.name())
                                .requestMatchers("/api/v1/summary/**").hasAuthority(Role.USER.name())
                                .requestMatchers("/api/v1/lesson/**").hasAuthority(Role.USER.name())
                                .requestMatchers("/api/v1/module/**").hasAuthority(Role.USER.name())
                                .requestMatchers("/api/v1/course/**").hasAuthority(Role.USER.name())
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
