package com.eduard.volchonokcore.security.jwt;

import com.eduard.volchonokcore.database.entities.User;
import com.eduard.volchonokcore.security.jwt.models.AccessToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
public class JwtService {
    @Value("${jwt.token.access.secret}")
    private String accessSecretStr;
    private SecretKey accessSecret;
    @Value("${jwt.token.refresh.secret}")
    private String refreshSecretStr;
    private SecretKey refreshSecret;
    @Value("${jwt.token.access.expired}")
    private long accessExp;

    @Value("${jwt.token.refresh.expired}")
    private long refreshExp;
    private String issuer;

    @PostConstruct
    public void doInit(){
        accessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretStr));
        refreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretStr));
    }
    public AccessToken generateAccessToken(UUID sessionUuid){
        Date now = new Date();
        Date expiration = new Date(now.getTime()+accessExp);

        Claims claims = Jwts.claims()
                .setSubject("access")
                .setIssuer(issuer)
                .setId(sessionUuid.toString())
                .setIssuedAt(now)
                .setExpiration(expiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(accessSecret)
                .compact();

        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(token);
        accessToken.setCreated(now);
        accessToken.setExpiresIn(expiration);
        return accessToken;
    }
    public String generateRefreshToken(UUID sessionUuid){
        Date now = new Date();
        Date expiration = new Date(now.getTime()+refreshExp);

        Claims claims = Jwts.claims()
                .setSubject("refresh")
                .setIssuer(issuer)
                .setId(sessionUuid.toString())
                .setIssuedAt(now)
                .setExpiration(expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(refreshSecret)
                .compact();
    }
    private boolean validateToken( String token,  Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }
    public boolean validateAccessToken(String accessToken){
        return validateToken(accessToken,accessSecret);
    }
    public boolean validateRefreshToken(String refreshToken){
        return validateToken(refreshToken,refreshSecret);
    }
    public Claims getAccessClaims( String accessToken) {
        return getClaims(accessToken, accessSecret);
    }

    public Claims getRefreshClaims( String refreshToken) {
        return getClaims(refreshToken, refreshSecret);
    }

    private Claims getClaims( String token,  Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UUID getSessionIdAccess(String accessToken){
        Claims claims = getAccessClaims(accessToken);
        return UUID.fromString(claims.getId());
    }
    public UUID getSessionIdRefresh(String refreshToken){
        Claims claims = getAccessClaims(refreshToken);
        return UUID.fromString(claims.getId());

    }

    public String resolveAccessToken(HttpServletRequest request){
        final String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }


    public String getUsername(String token){
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getClaims(token, accessSecret);
        return claimsResolver.apply(claims);
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername())) && validateToken(token,accessSecret);
    }
}
