package com.eduard.volchonokcore.security.jwt;

import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.services.SessionService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private SessionService sessionService;
    public JwtFilter(JwtService jwtService, SessionService sessionService) {
        this.jwtService = jwtService;
        this.sessionService = sessionService;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            log.info("Auth header is null");
            filterChain.doFilter(request,response);
            return;
        }
        String accessToken = jwtService.resolveAccessToken(request);
        if(accessToken == null || !jwtService.validateAccessToken(accessToken)){
            log.info("Invalid JWT");
            filterChain.doFilter(request, response);
            return;
        }
        Session session = sessionService.findByUuid(jwtService.getSessionIdAccess(accessToken));
        if(session == null){
            log.info("Session is null");
            filterChain.doFilter(request,response);
            return;
        }
        Claims claims = jwtService.getAccessClaims(accessToken);
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(session,session.getUser());
        Date iat = claims.getIssuedAt();
        if(!iat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(session.getLastRefresh())){
            authentication.setAuthenticated(true);
        }else{
            log.info("Session expired");
            authentication.setAuthenticated(false);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
