package com.school.management.application.api.filter;

import com.nimbusds.jose.proc.SecurityContext;
import com.school.management.application.api.auth.AuthenticatedUser;
import com.school.management.application.exceptions.UnauthenticatedException;
import com.school.management.application.services.AuthenticationService;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieAuthFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(CookieAuthFilter.class);
    @Autowired
    private AuthenticationService authenticationService;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return antPathMatcher.match("/api/v1/auth/**", request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        this.logger.info("CookieAuthFilter Filter");
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            filterChain.doFilter(request, response); // Config authenticated request like auth/ /language
            return;
        }

        String domain = "app";

        Optional<Cookie> authCookie = resolveAuthenticationCookieForDomain(request, domain);

        if (authCookie.isEmpty() || authCookie.get().getValue().isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        Jws<Claims> parsedJwt;

        try {
            parsedJwt = authenticationService.parseJwt(authCookie.get().getValue());
        } catch (UnsupportedJwtException | MalformedJwtException | CompressionException | IllegalArgumentException e) {
            throw UnauthenticatedException.MALFORMED_TOKEN;
        } catch (ExpiredJwtException e) {
            throw UnauthenticatedException.EXPIRED_TOKEN;
        } catch (SignatureException e) {
            throw UnauthenticatedException.INVALID_TOKEN;
        }

        String tokenType = Optional.ofNullable(parsedJwt.getBody().get("typ", String.class)).orElseThrow(() -> UnauthenticatedException.INVALID_TOKEN);

        if (!tokenType.equals("access")) {
            throw UnauthenticatedException.INVALID_CREDENTIALS;
        }

        try {
            SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(parsedJwt));
//            requestContext.setUserId(((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()).getUserId());
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
//            requestContext.clear();
        }
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    private Optional<Cookie> resolveAuthenticationCookieForDomain(HttpServletRequest request, String domain) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("app.token")) {
                return Optional.of(cookie);
            }
        }

        return Optional.empty();

    }
}
