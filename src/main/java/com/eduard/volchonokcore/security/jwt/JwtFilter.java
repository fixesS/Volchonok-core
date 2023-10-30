package com.eduard.volchonokcore.security.jwt;

import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.services.SessionService;
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
        log.info("Filtered");
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String accessToken = jwtService.resolveAccessToken(request);
        Session session = sessionService.findByUuid(jwtService.getSessionIdAccess(accessToken));
        if(session == null){
            log.error("Session is null");
            filterChain.doFilter(request,response);
            return;
        }
        Claims claims = jwtService.getAccessClaims(accessToken);
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(session,session.getUser());
        Date iat = claims.getIssuedAt();
        if(!iat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(session.getLastRefresh())){
            authentication.setAuthenticated(true);
        }else{
            authentication.setAuthenticated(false);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
