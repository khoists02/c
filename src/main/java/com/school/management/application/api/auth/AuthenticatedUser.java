package com.school.management.application.api.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.time.ZonedDateTime;
import java.util.*;

public class AuthenticatedUser implements Authentication {
    private UUID userId;
    private String username;
    private UUID sessionId;
    private List<? extends GrantedAuthority> grantedAuthorities = new ArrayList<>();
    private final Jws<Claims> parsedJwt;
//    private final ZonedDateTime trialExpiresAt;
//    private final boolean readOnly;
//    private final String dataAccountId;
    private List<UUID> allowedOrganisations = null;

    public AuthenticatedUser(Jws<Claims> parsedJwt) {
        this.parsedJwt = parsedJwt;
        this.userId = UUID.fromString(parsedJwt.getBody().get("sub", String.class));
        this.username = parsedJwt.getBody().get("un", String.class);
//        this.sessionId = UUID.fromString(parsedJwt.getBody().get("ses", String.class));
//        this.trialExpiresAt = Optional.ofNullable(parsedJwt.getBody().get("tme", String.class))
//                .filter(s -> !s.isBlank()).map(ZonedDateTime::parse).orElse(null);
//        this.readOnly = parsedJwt.getBody().get("ro", Boolean.class);
//        this.dataAccountId = parsedJwt.getBody().get("da", String.class);

//        if (!Optional.ofNullable(parsedJwt.getBody().get("oacl", Boolean.class)).orElse(true)) {
//            allowedOrganisations = List.of();
//        }
    }

    public String getAllowedOrigin () { return this.parsedJwt.getBody().get("dom", String.class); }

    public UUID getSessionId () {
        return this.sessionId;
    }

    public <T> T getClaimValue(String claim, Class<T> clazz) { return this.parsedJwt.getBody().get(claim, clazz); }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    public UUID getUserId() { return this.userId; }

    public String getUsername() { return this.username; }


//    public Boolean getTrialExpired() { return this.trialExpiresAt.isBefore(ZonedDateTime.now()); }
//
//    public Boolean isReadOnly () { return this.readOnly; }

    @Override
    public String getName() {
        return this.username;
    }
}
