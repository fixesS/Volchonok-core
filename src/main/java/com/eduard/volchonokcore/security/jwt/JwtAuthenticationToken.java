package com.eduard.volchonokcore.security.jwt;

import com.eduard.volchonokcore.database.entities.Session;
import com.eduard.volchonokcore.database.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken implements Authentication {

    private boolean authenticated;
    private Session session;
    private User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return user.getUserId();
    }

    @Override
    public Object getPrincipal() {
        return session.getSessionUuid();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return null;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public JwtAuthenticationToken(Session session, User user ) {
        this.session = session;
        this.user = user;

    }

}
