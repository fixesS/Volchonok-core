package com.eduard.volchonokcore.security;

import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.database.services.SessionService;
import com.eduard.volchonokcore.security.jwt.JwtService;
import com.eduard.volchonokcore.security.jwt.models.AccessToken;
import com.eduard.volchonokcore.web.models.TokenData;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.time.ZoneId;
import java.util.UUID;

public class AuthService {

    private JwtService jwtService;
    @Autowired
    public SessionService sessionService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public TokenData createSession(User user, String userAgent, InetAddress address){

        Session session = new Session();
        UUID sessionUuid = UUID.randomUUID();

        session.setUserAgent(userAgent);
        session.setSessionUuid(sessionUuid);
        session.setIp(address);
        session.setUser(user);

        AccessToken accessToken = jwtService.generateAccessToken(session.getSessionUuid());

        session.setCreated(accessToken.getCreated().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        session.setExpiresIn(accessToken.getExpiresIn().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        session.setLastRefresh(accessToken.getCreated().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        sessionService.create(session);

        TokenData tokenData = new TokenData();
        tokenData.setAccess_token(accessToken.getAccessToken());
        tokenData.setRefresh_token(jwtService.generateRefreshToken(session.getSessionUuid()));

        return tokenData;
    }

    public TokenData refreshSession(UUID sessionUuid){

        Session session = sessionService.findByUuid(sessionUuid);

        AccessToken accessToken = jwtService.generateAccessToken(sessionUuid);

        session.setLastRefresh(accessToken.getCreated().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        session.setExpiresIn(accessToken.getExpiresIn().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        sessionService.update(session);

        TokenData tokenData = new TokenData();
        tokenData.setAccess_token(accessToken.getAccessToken());
        tokenData.setRefresh_token(jwtService.generateRefreshToken(sessionUuid));

        sessionService.removeExpired();

        return tokenData;
    }


}

