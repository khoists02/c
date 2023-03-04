package com.school.management.application.services;

import com.school.management.application.exceptions.UnauthenticatedException;
import com.school.management.application.model.User;
import com.school.management.application.model.UserSession;
import com.school.management.application.repositories.UserSessionRepository;
import com.school.management.application.repositories.UsersRepository;
import com.school.management.application.utils.RequestUtils;
import com.school.management.application.utils.UserUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;


@Service
public class AuthenticationService {

    @Autowired
    private ConfigurationService configuration;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RequestUtils requestUtils;

    private JwtParser jwtParser;


    @PostConstruct
    protected void init() {
        this.jwtParser =  Jwts.parser();
    }

    @Getter
    @Builder
    public static class AuthenticationContext {
        private String username;
        private String password;
    }

    public void extendUserSession(UserSession session) {
        if (session.getPermitExtension()) session.setExpiresAt(
                ZonedDateTime
                        .now()
                        .plus(configuration.getRefreshTokenValidMinutes(), ChronoUnit.MINUTES)
        );
        session.setIp(RequestUtils.getClientIpAddress());
        session.setUserAgent(RequestUtils.getHeader("User-Agent", ""));
    }
    public UserSession createUserSession(
            AuthenticationContext authenticationContext,
            User user
    ) {
        UserSession session = new UserSession();
        this.extendUserSession(session);
        session.setUser(user);
        return userSessionRepository.save(session);
    }

    public String generateAccessToken(
            User user,
            UserSession session
    ) {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 60 * 30)); // 30 mn
        return Jwts
                .builder()
                .setSubject(user.getId().toString())
                .claim("typ", "access")
                .claim("un", user.getUsername())
                .setAudience("20230203")
                .setIssuer("20230203")
                .setIssuedAt(now)
                .setExpiration(exp)
                .setHeaderParam("kid", "kid")
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    public String generateRefreshToken(
            User user,
            UserSession session
    ) {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 60 * 10)); // 10 mn
        return Jwts
                .builder()
                .setSubject(user.getId().toString())
                .claim("typ", "refresh")
                .claim("un", user.getUsername())
                .setAudience("20230203")
                .setIssuer("20230203")
                .setIssuedAt(now)
                .setExpiration(exp)
                .setHeaderParam("kid", "kid")
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }


    public void authenticate(AuthenticationContext context) {
        User user = usersRepository
                .findOneByUsername(context.getUsername())
                .orElseThrow(() -> UnauthenticatedException.INVALID_CREDENTIALS);

        if (!passwordEncoder.matches(context.getPassword(), user.getPassword())
        ) {
            throw UnauthenticatedException.INVALID_CREDENTIALS;
        }

        //Create the JWT and Refresh Token
        UserSession session = this.createUserSession(context, user);
        String accessToken = this.generateAccessToken(user, session);
        String refreshToken = this.generateRefreshToken(user, session);

        //Set the cookies
        this.injectAuthenticationTokenCookie(
                response,
                accessToken
        );
        this.injectRefreshTokenCookie(
                response,
                refreshToken
        );
    }

    private String getKey() { return  Base64.getEncoder().encodeToString(configuration.getTokenIssuer().getBytes()); }

    public void injectAuthenticationTokenCookie(
            HttpServletResponse response,
            String cookieValue
    ) {
        Cookie tokenCookie = new Cookie("app.token", cookieValue);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setSecure(configuration.getCookieSecure());
        tokenCookie.setDomain(configuration.getCookieDomain());
        response.addCookie(tokenCookie);
    }

    public Jws<Claims> parseJwt(String token) {
        return this.jwtParser.setSigningKey(getKey()).parseClaimsJws(token);
    }

    public void injectRefreshTokenCookie(
            HttpServletResponse response,
            String cookieValue
    ) {
        Cookie refreshCookie = new Cookie("app.refresh", cookieValue);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/auth");
        refreshCookie.setDomain(configuration.getCookieDomain());
        refreshCookie.setSecure(configuration.getCookieSecure());
        response.addCookie(refreshCookie);
    }

    @Transactional(readOnly = true)
    public boolean hasPermission(String permission) {
        return usersRepository.hasPermission(UserUtils.getUser(), permission);
    }
}
